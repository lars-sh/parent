package de.larssh.utils.net;

import static java.util.Arrays.asList;

import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import de.larssh.utils.Nullables;
import de.larssh.utils.text.SplitLimit;
import de.larssh.utils.text.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Proxy related properties, described at <a href=
 * "https://docs.oracle.com/javase/8/docs/api/java/net/doc-files/net-properties.html">Network
 * Properties</a>.
 *
 * <p>
 * <b>Usage example 1:</b> The following shows how to set a global HTTP proxy.
 *
 * <pre>
 * InetSocketAddress inetSocketAddress = new InetSocketAddress("proxy.example.com", "8080");
 * GlobalProxyConfiguration.HTTP.setGlobalProxy(inetSocketAddress);
 * </pre>
 *
 * <p>
 * <b>Usage example 2:</b> The following shows how to unset the global HTTP
 * proxy.
 *
 * <pre>
 * GlobalProxyConfiguration.HTTP.unsetGlobalProxy();
 * </pre>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GlobalProxyConfiguration {
	/**
	 * Default {@code nonProxyHosts} for {@link #FTP} and {@link #HTTP}
	 */
	private static final Collection<String> NO_PROXY_HOSTS_DEFAULT = asList("localhost", "127.*", "[::1]");

	/**
	 * FTP proxy related settings
	 */
	public static final GlobalProxyConfiguration FTP = new GlobalProxyConfiguration("FTP",
			Type.HTTP,
			80,
			"ftp.proxyHost",
			"ftp.proxyPort",
			Optional.of("ftp.nonProxyHosts"),
			Optional.of(NO_PROXY_HOSTS_DEFAULT));

	/**
	 * HTTP proxy related settings
	 */
	public static final GlobalProxyConfiguration HTTP = new GlobalProxyConfiguration("HTTP",
			Type.HTTP,
			80,
			"http.proxyHost",
			"http.proxyPort",
			Optional.of("http.nonProxyHosts"),
			Optional.of(NO_PROXY_HOSTS_DEFAULT));

	/**
	 * HTTPS proxy related settings
	 *
	 * <p>
	 * The HTTPS protocol handler will use the same {@code nonProxyHosts} property
	 * as the HTTP protocol. Use {@link #HTTP} to modify that property.
	 */
	public static final GlobalProxyConfiguration HTTPS = new GlobalProxyConfiguration("HTTPS",
			Type.HTTP,
			443,
			"https.proxyHost",
			"https.proxyPort",
			Optional.empty(),
			Optional.empty());

	/**
	 * SOCKS proxy related properties
	 *
	 * <p>
	 * The SOCKS protocol handler does not support the {@code nonProxyHosts}
	 * property.
	 */
	public static final GlobalProxyConfiguration SOCKS = new GlobalProxyConfiguration("SOCKS",
			Type.SOCKS,
			1080,
			"socksProxyHost",
			"socksProxyPort",
			Optional.empty(),
			Optional.empty());

	/**
	 * Name
	 *
	 * @return name
	 */
	String name;

	/**
	 * Proxy type
	 *
	 * @return proxy type
	 */
	Type type;

	/**
	 * Default proxy port
	 *
	 * @return default proxy port
	 */
	int defaultPort;

	/**
	 * Host property name
	 *
	 * @return host property name
	 */
	String hostProperty;

	/**
	 * Port property name
	 *
	 * @return port property name
	 */
	String portProperty;

	/**
	 * {@code nonProxyHosts} property name
	 *
	 * @return {@code nonProxyHosts} property name
	 */
	Optional<String> nonProxyHostsProperty;

	/**
	 * Default {@code nonProxyHosts}
	 *
	 * @return default {@code nonProxyHosts}
	 */
	Optional<Collection<String>> defaultNonProxyHosts;

	/**
	 * Appends all of {@code nonProxyHosts} to the current protocols
	 * {@code nonProxyHosts} property if it is not already present.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @param nonProxyHosts any number of hosts that should be accessed
	 *                      <b>without</b> going through the proxy
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	public void addNonProxyHosts(final String... nonProxyHosts) {
		final Set<String> set = getNonProxyHosts();
		set.addAll(asList(nonProxyHosts));
		setNonProxyHosts(set);
	}

	/**
	 * Returns all patterns of the current protocols {@code nonProxyHosts} property.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @return patterns of the current protocols {@code nonProxyHosts} property
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	public Set<String> getNonProxyHosts() {
		return new LinkedHashSet<>(asList(Nullables.orElse(System.getProperty(getNonProxyHostsPropertyOrThrow()), "")
				.split("\\|", SplitLimit.NONE_AND_NO_EMPTY_TRAILING)));
	}

	/**
	 * Returns the current protocols {@code nonProxyHosts} property name.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @return the current protocols {@code nonProxyHosts} property name
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	protected String getNonProxyHostsPropertyOrThrow() {
		return getNonProxyHostsProperty().orElseThrow(() -> new UnsupportedOperationException(
				Strings.format("The protocol %s does not support the nonProxyHosts property.", getName())));
	}

	/**
	 * Removes all given patterns from the current protocols {@code nonProxyHosts}
	 * property.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @param nonProxyHosts any number of hosts that should not be accessed
	 *                      <b>with</b> going through the proxy
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	public void removeNonProxyHosts(final String... nonProxyHosts) {
		final Set<String> set = getNonProxyHosts();
		set.removeAll(asList(nonProxyHosts));
		setNonProxyHosts(set);
	}

	/**
	 * Resets the current protocols {@code nonProxyHosts} property.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * <p>
	 * Afterwards nothing but the default {@code nonProxyHosts} are set.
	 *
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	public void resetNonProxyHosts() {
		setNonProxyHosts(getDefaultNonProxyHosts().orElseGet(Collections::emptyList));
	}

	/**
	 * Overwrites the JVM global current protocols proxy properties with the given
	 * host and port.
	 *
	 * @param inetSocketAddress host and port to apply globally
	 */
	public void setGlobalProxy(final InetSocketAddress inetSocketAddress) {
		System.setProperty(getHostProperty(), inetSocketAddress.getHostString());
		System.setProperty(getPortProperty(), Integer.toString(inetSocketAddress.getPort()));
	}

	/**
	 * Overwrites the current protocols {@code nonProxyHosts} property with the
	 * given patterns.
	 *
	 * <p>
	 * For protocols not supporting the {@code nonProxyHosts} property an
	 * {@link UnsupportedOperationException} is thrown.
	 *
	 * @param nonProxyHosts any number of hosts that should be accessed
	 *                      <b>without</b> going through the proxy
	 * @throws UnsupportedOperationException for protocols not supporting the
	 *                                       {@code nonProxyHosts} property
	 */
	public void setNonProxyHosts(final Collection<String> nonProxyHosts) {
		System.setProperty(getNonProxyHostsPropertyOrThrow(), String.join("|", nonProxyHosts));
	}

	/**
	 * Overwrites the JVM global current protocols proxy properties.
	 */
	public void unsetGlobalProxy() {
		System.clearProperty(getHostProperty());
		System.clearProperty(getPortProperty());
	}
}

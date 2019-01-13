package de.larssh.utils.maven;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.execution.MavenSession;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.eclipse.aether.util.repository.AuthenticationBuilder;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for Aether.
 */
@UtilityClass
public class AetherUtils {
	/**
	 * Converts {@code mavenRepository} to an Aether Remote Repository.
	 *
	 * @param mavenRepository Maven Artifact Repository
	 * @return Aether Remote Repository
	 */
	public static RemoteRepository convert(final ArtifactRepository mavenRepository) {
		final ArtifactRepository r = mavenRepository;
		return new RemoteRepository.Builder(r.getId(), r.getLayout().getId(), r.getUrl())
				.setAuthentication(convert(r.getAuthentication()))
				.setProxy(convert(r.getProxy()))
				.setReleasePolicy(convert(r.getReleases()))
				.setSnapshotPolicy(convert(r.getSnapshots()))
				.build();
	}

	/**
	 * Converts {@code mavenPolicy} to an Aether Repository Policy.
	 *
	 * @param mavenPolicy Maven Artifact Repository Policy
	 * @return Aether Repository Policy
	 */
	@Nullable
	public static RepositoryPolicy convert(@Nullable final ArtifactRepositoryPolicy mavenPolicy) {
		return Optional.ofNullable(mavenPolicy)
				.map(p -> new RepositoryPolicy(p.isEnabled(), p.getUpdatePolicy(), p.getChecksumPolicy()))
				.orElse(null);
	}

	/**
	 * Converts {@code mavenAuthentication} to an Aether Authentication.
	 *
	 * @param mavenAuthentication Maven Authentication
	 * @return Aether Authentication
	 */
	@Nullable
	public static Authentication convert(
			@Nullable final org.apache.maven.artifact.repository.Authentication mavenAuthentication) {
		return Optional.ofNullable(mavenAuthentication)
				.map(a -> new AuthenticationBuilder().addPassword(a.getPassword()).addUsername(a.getUsername()).build())
				.orElse(null);
	}

	/**
	 * Converts {@code mavenProxy} to an Aether Proxy.
	 *
	 * @param mavenProxy Maven Proxy
	 * @return Aether Proxy
	 */
	@Nullable
	public static Proxy convert(@Nullable final org.apache.maven.repository.Proxy mavenProxy) {
		if (mavenProxy == null) {
			return null;
		}

		final Authentication authentication
				= new AuthenticationBuilder().addHostnameVerifier(createHostnameVerifier(mavenProxy.getNonProxyHosts()))
						.addNtlm(mavenProxy.getNtlmHost(), mavenProxy.getNtlmDomain())
						.addPassword(mavenProxy.getPassword())
						.addUsername(mavenProxy.getUserName())
						.build();

		return new Proxy(mavenProxy.getProtocol(), mavenProxy.getHost(), mavenProxy.getPort(), authentication);
	}

	/**
	 * Creates a {@link HostnameVerifier} for {@code nonProxyHosts} matching the JDK
	 * configuration equivalent.
	 *
	 * @param nonProxyHosts list of non proxy host patterns separated by {@code |}
	 * @return created {@link HostnameVerifier} object
	 */
	@Nullable
	private static HostnameVerifier createHostnameVerifier(@Nullable final String nonProxyHosts) {
		if (nonProxyHosts == null) {
			return null;
		}

		final List<Pattern> patterns = new ArrayList<>();
		final StringTokenizer tokenizer = new StringTokenizer(nonProxyHosts, "|");
		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken();
			patterns.add(Pattern.compile(token.replace(".", "\\.").replace("*", ".*"), Pattern.CASE_INSENSITIVE));
		}

		return (hostname, session) -> patterns.stream().noneMatch(pattern -> pattern.matcher(hostname).matches());
	}

	/**
	 * Retrieves the class path of resolved dependencies.
	 *
	 * @param dependencyResult resolved dependencies
	 * @return class path
	 */
	public static String getClassPath(final DependencyResult dependencyResult) {
		final PreorderNodeListGenerator preorderNodeListGenerator = new PreorderNodeListGenerator();
		dependencyResult.getRoot().accept(preorderNodeListGenerator);
		return preorderNodeListGenerator.getClassPath();
	}

	/**
	 * Retrieves a list of repositories configured for {@code mavenSession}.
	 *
	 * @param mavenSession Maven Session
	 * @return list of repositories
	 */
	public static List<RemoteRepository> getRemoteRepositories(final MavenSession mavenSession) {
		return mavenSession.getRequest().getRemoteRepositories().stream().map(AetherUtils::convert).collect(toList());
	}
}

package de.larssh.utils.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import de.larssh.utils.SneakyException;
import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * Utility class to simplify the handling of connection certificates.
 *
 * <p>
 * Method {@link #getSocketFactoryTrusting(Path, String)} highly simplifies
 * trusting custom certificates by creating a {@link SSLSocketFactory} directly
 * from a JKS (Java Key Store) file. Those key stores can be created and
 * maintained using e.g. the <a href="https://keystore-explorer.org/">KeyStore
 * Explorer</a>.
 *
 * <pre>
 * Path jksFilePath = ...;
 * String password = ...;
 * SSLSocketFactory socketFactory = SecureConnections.getSocketFactoryTrusting(jksFilePath, password);
 *
 * URL url = ...;
 * URLConnection urlConnection = url.openConnection();
 *
 * SecureConnections.getHttpsUrlConnection(urlConnection)
 *   .ifPresent(httpsUrlConnection -&gt; httpsUrlConnection.setSSLSocketFactory(socketFactory));
 * </pre>
 */
@UtilityClass
public class SecureConnections {
	/**
	 * Returns {@code connection} if it is of type {@link HttpsURLConnection} or
	 * {@link Optional#empty()}.
	 *
	 * @param connection the connection to handle
	 * @return {@code connection} if it is of type {@link HttpsURLConnection} or
	 *         {@link Optional#empty()}
	 */
	public static Optional<HttpsURLConnection> getHttpsUrlConnection(final URLConnection connection) {
		return connection instanceof HttpsURLConnection
				? Optional.of((HttpsURLConnection) connection)
				: Optional.empty();
	}

	/**
	 * Returns an array of {@link KeyManager}s for a given key store and password.
	 *
	 * @param keyStore key store
	 * @param password key store password
	 * @return array of {@link KeyManager}s
	 * @throws UnrecoverableKeyException if the key cannot be recovered (e.g. the
	 *                                   given password is wrong).
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exceptions that must not be thrown")
	public static KeyManager[] getKeyManagers(final KeyStore keyStore, final String password)
			throws UnrecoverableKeyException {
		try {
			final KeyManagerFactory keyManagerFactory
					= KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, password.toCharArray());
			return keyManagerFactory.getKeyManagers();
		} catch (final KeyStoreException | NoSuchAlgorithmException e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Creates a {@link SSLSocketFactory} based on key managers.
	 *
	 * @param keyManagers key manager
	 * @return SSL socket factory
	 */
	@SuppressWarnings("PMD.UseVarargs")
	@SuppressFBWarnings(value = "UVA_USE_VAR_ARGS",
			justification = "var args make no sense as KeyManager is handled as array regularly")
	public static SSLSocketFactory getSocketFactory(final KeyManager[] keyManagers) {
		return getSocketFactory(keyManagers, null);
	}

	/**
	 * Creates a {@link SSLSocketFactory} based on trust managers.
	 *
	 * @param trustManagers trust manager
	 * @return SSL socket factory
	 */
	@SuppressWarnings("PMD.UseVarargs")
	@SuppressFBWarnings(value = "UVA_USE_VAR_ARGS",
			justification = "var args make no sense as TrustManager is handled as array regularly")
	public static SSLSocketFactory getSocketFactory(final TrustManager[] trustManagers) {
		return getSocketFactory(null, trustManagers);
	}

	/**
	 * Creates a {@link SSLSocketFactory} based on key managers and trust managers.
	 *
	 * @param keyManagers   key manager
	 * @param trustManagers trust manager
	 * @return SSL socket factory
	 */
	@SuppressWarnings("PMD.UseVarargs")
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exceptions that must not be thrown")
	public static SSLSocketFactory getSocketFactory(@Nullable final KeyManager[] keyManagers,
			@Nullable final TrustManager[] trustManagers) {
		try {
			final SSLContext context = SSLContext.getInstance("TLS");
			context.init(keyManagers, trustManagers, null);
			return context.getSocketFactory();
		} catch (final KeyManagementException | NoSuchAlgorithmException e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Creates a {@link SSLSocketFactory} trusting the certificates inside JKS (Java
	 * Key Store) file.
	 *
	 * @param jksFilePath path to the key store
	 * @param password    key store password
	 * @return SSL socket factory
	 * @throws CertificateException if any of the certificates in the key store
	 *                              could not be loaded
	 * @throws IOException          if an I/O error occurs
	 */
	public static SSLSocketFactory getSocketFactoryTrusting(final Path jksFilePath, final String password)
			throws CertificateException, IOException {
		return getSocketFactory(getTrustManagers(loadKeyStore(jksFilePath, password)));
	}

	/**
	 * Returns an array of {@link TrustManager}s for a given key store and password.
	 *
	 * @param keyStore key store
	 * @return array of {@link TrustManager}s
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exceptions that must not be thrown")
	public static TrustManager[] getTrustManagers(final KeyStore keyStore) {
		try {
			final TrustManagerFactory trustManagerFactory
					= TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);
			return trustManagerFactory.getTrustManagers();
		} catch (final KeyStoreException | NoSuchAlgorithmException e) {
			throw new SneakyException(e);
		}
	}

	/**
	 * Loads a JKS (Java Key Store) file.
	 *
	 * @param jksFilePath path to the key store
	 * @param password    key store password
	 * @return key store
	 * @throws CertificateException if any of the certificates in the key store
	 *                              could not be loaded
	 * @throws IOException          if an I/O error occurs
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exceptions that must not be thrown")
	public static KeyStore loadKeyStore(final Path jksFilePath, final String password)
			throws CertificateException, IOException {
		try (InputStream inputStream = Files.newInputStream(jksFilePath)) {
			final KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(inputStream, password.toCharArray());
			return keyStore;
		} catch (final KeyStoreException | NoSuchAlgorithmException e) {
			throw new SneakyException(e);
		}
	}
}

package de.larssh.utils.net;

import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * Utility class to handle SSL/TLS connections in <b>insecure</b> ways. Its
 * usage is <b>NOT</b> appreciated for security reasons!
 */
@UtilityClass
public class InsecureConnections {
	/**
	 * Array of {@link TrustManager}s that <b>do not verify certificates</b>
	 */
	private static final TrustManager[] TRUST_MANAGERS_THAT_DO_NOT_VERIFY_CERTIFICATES
			= new TrustManager[] { new TrustManagerThatDoesNotVerifyCertificates() };

	/**
	 * {@link HostnameVerifier} that <b>does not verify host names</b>
	 */
	private static final HostnameVerifier HOST_NAME_VERIFIER_THAT_DOES_NOT_VERIFY_HOSTNAMES
			= (hostname, session) -> true;

	/**
	 * Returns an array of {@link TrustManager}s that <b>do not verify
	 * certificates</b>. Its usage is <b>NOT</b> appreciated for security reasons!.
	 *
	 * @return array of {@link TrustManager}s that <b>do not verify certificates</b>
	 */
	public static TrustManager[] getTrustManagersThatDoNotVerifyCertificates() {
		return TRUST_MANAGERS_THAT_DO_NOT_VERIFY_CERTIFICATES;
	}

	/**
	 * Returns a {@link HostnameVerifier} that <b>does not verify host names</b>.
	 * Its usage is <b>NOT</b> appreciated for security reasons!.
	 *
	 * @return a {@link HostnameVerifier} that <b>does not verify host names</b>
	 */
	public static HostnameVerifier getHostNameVerifierThatDoesNotVerifyHostNames() {
		return HOST_NAME_VERIFIER_THAT_DOES_NOT_VERIFY_HOSTNAMES;
	}

	/**
	 * {@link TrustManager} that <b>does not verify certificates</b>
	 */
	@NoArgsConstructor
	private static final class TrustManagerThatDoesNotVerifyCertificates implements X509TrustManager {
		/**
		 * Empty array of acceptable CA issuer certificates
		 */
		private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

		/** {@inheritDoc} */
		@Override
		public void checkClientTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType) {
			// do not verify client certificates
		}

		/** {@inheritDoc} */
		@Override
		public void checkServerTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType) {
			// do not verify server certificates
		}

		/** {@inheritDoc} */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return ACCEPTED_ISSUERS;
		}
	}
}

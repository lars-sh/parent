package de.larssh.utils.net;

import java.net.Socket;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import edu.umd.cs.findbugs.annotations.Nullable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * Utility class to handle SSL/TLS connections in <b>insecure</b> ways. Its
 * usage is <b>NOT</b> appreciated for security reasons!
 */
@UtilityClass
public class InsecureConnections {
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
	@SuppressWarnings("PMD.TRUST_MANAGERS_THAT_DO_NOT_VERIFY_CERTIFICATES")
	public static TrustManager[] getTrustManagersThatDoNotVerifyCertificates() {
		return new TrustManager[] { TrustManagerThatDoesNotVerifyCertificates.getInstance() };
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
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static final class TrustManagerThatDoesNotVerifyCertificates extends X509ExtendedTrustManager {
		/**
		 * Empty array of acceptable CA issuer certificates
		 */
		private static final X509Certificate[] ACCEPTED_ISSUERS = {};

		/**
		 * Singleton instance
		 */
		private static final TrustManager INSTANCE = new TrustManagerThatDoesNotVerifyCertificates();

		/**
		 * {@link TrustManager} that <b>does not verify certificates</b>
		 *
		 * @return {@link TrustManager} that <b>does not verify certificates</b>
		 */
		public static TrustManager getInstance() {
			return INSTANCE;
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkClientTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType) {
			// do not verify client certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkClientTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType,
				@SuppressWarnings("unused") @Nullable final Socket socket) {
			// do not verify client certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkClientTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType,
				@SuppressWarnings("unused") @Nullable final SSLEngine engine) {
			// do not verify client certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkServerTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType) {
			// do not verify server certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkServerTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType,
				@SuppressWarnings("unused") @Nullable final Socket socket) {
			// do not verify server certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public void checkServerTrusted(@SuppressWarnings("unused") @Nullable final X509Certificate[] chain,
				@SuppressWarnings("unused") @Nullable final String authenticationType,
				@SuppressWarnings("unused") @Nullable final SSLEngine engine) {
			// do not verify server certificates
		}

		/** {@inheritDoc} */
		@Override
		@SuppressFBWarnings(value = "WEAK_TRUST_MANAGER",
				justification = "The check is absolutely right. That's why this is part of the utility class InsecureConnections.")
		public X509Certificate[] getAcceptedIssuers() {
			return ACCEPTED_ISSUERS;
		}
	}
}

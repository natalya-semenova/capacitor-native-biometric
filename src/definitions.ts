export enum BiometryType {
  NONE,
  TOUCH_ID,
  FACE_ID,
  FINGERPRINT,
  FACE_AUTHENTICATION,
  IRIS_AUTHENTICATION,
  MULTIPLE,
}

export interface Credentials {
  username: string;
  password: string;
}

export interface IsAvailableOptions {
  /**
   * Specifies if should fallback to passcode authentication if biometric authentication is not available.
   */
  useFallback: boolean;
}

export interface AvailableResult {
  isAvailable: boolean;
  biometryType: BiometryType;
  errorCode?: number;
}

export interface BiometricOptions {
  reason?: string;
  title?: string;
  subtitle?: string;
  description?: string;
  negativeButtonText?: string;
  useFallback?: boolean;
  /**
   * Only for Android.
   * Set a maximum number of attempts for biometric authentication. The maximum allowed by android is 5.
   * @default 1
   */
  maxAttempts?: number;
}

export interface GetCredentialOptions {
  server: string;
}

export interface SetCredentialOptions {
  username: string;
  password: string;
  server: string;
}

export interface DeleteCredentialOptions {
  server: string;
}

export interface NativeBiometricPlugin {
  /**
   * Checks if biometric authentication hardware is available.
   * @param {IsAvailableOptions} [options]
   * @returns {Promise<AvailableResult>}
   * @memberof NativeBiometricPlugin
   * @since 1.0.0
   */
  isAvailable(options?: IsAvailableOptions): Promise<AvailableResult>;
  /**
   * Prompts the user to authenticate with biometrics.
   * @param {BiometricOptions} [options]
   * @returns {Promise<any>}
   * @memberof NativeBiometricPlugin
   * @since 1.0.0
   */
  verifyIdentity(options?: BiometricOptions): Promise<any>;
  /**
   * Gets the stored credentials for a given server.
   * @param {GetCredentialOptions} options
   * @returns {Promise<Credentials>}
   * @memberof NativeBiometricPlugin
   * @since 1.0.0
   */
  getCredentials(options: GetCredentialOptions): Promise<Credentials>;
  /**
   * Stores the given credentials for a given server.
   * @param {SetCredentialOptions} options
   * @returns {Promise<any>}
   * @memberof NativeBiometricPlugin
   * @since 1.0.0
   */
  setCredentials(options: SetCredentialOptions): Promise<any>;
  /**
   * Deletes the stored credentials for a given server.
   * @param {DeleteCredentialOptions} options
   * @returns {Promise<any>}
   * @memberof NativeBiometricPlugin
   * @since 1.0.0
   */
  deleteCredentials(options: DeleteCredentialOptions): Promise<any>;
}

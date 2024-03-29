package me.digi.sdk

abstract class DMEError(override val message: String): Throwable(message)

sealed class DMESDKError(override val message: String): DMEError(message) {

    class NoContract(): DMESDKError("Contract ID not set.")
    class InvalidContract(): DMESDKError("Contract ID is not in a valid format.")
    class DecryptionFailed(): DMESDKError("Could not decrypt the file content.")
    class InvalidData(): DMESDKError("Could not deserialise the file content.")
    class InvalidVersion(): DMESDKError("Current version of SDK no longer supported.")
    class NoAppID(): DMESDKError("App ID not set.")
    class P12ParsingError(): DMESDKError("Could not parse the P12 file with supplied password, or no P12/password given.")
    class NoURLScheme(): DMESDKError("Intent filter for URL scheme (for callbacks) not found.")
    class DigiMeAppNotFound(): DMESDKError("Querying digime schema failed. (digi.me app not installed.)")
    class CommunicatorNotInitialized(): DMESDKError("DMEAppCommunicator shared instance accessed before initialization.")
    class InvalidContext(): DMESDKError("Given context is not the application context; ONLY the application context may be used.")

}

sealed class DMEAuthError(override val message: String): DMEError(message) {

    class General(): DMEAuthError("An unknown authorisation error has occurred.")
    class Cancelled(): DMEAuthError("The user cancelled the authorisation action.")
    class InvalidSession(): DMEAuthError("The session key is invalid or has expired.")
    class InvalidSessionKey(): DMEAuthError("The session key provided to the digi.me app is not valid.")

}

sealed class DMEAPIError(override val message: String): DMEError(message) {

    class Generic(override val message: String): DMEAPIError(message)
    class Unreachable(): DMEAPIError("Couldn't reach the digi.me API - please check your network connection.")

}
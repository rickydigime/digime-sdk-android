![](https://i.imgur.com/o1CNZZr.png)

<p align="center">
    <a href="https://digime-api.slack.com/">
        <img src="https://img.shields.io/badge/chat-slack-blueviolet.svg" alt="Developer Chat">
    </a>
    <a href="LICENSE">
        <img src="https://img.shields.io/badge/license-apache 2.0-blue.svg" alt="MIT License">
    </a>
    <a href="#">
    	<img src="https://img.shields.io/badge/build-passing-brightgreen.svg" 
    </a>
    <a href="https://swift.org">
        <img src="https://img.shields.io/badge/language-kotlin/java-ff69b4.svg" alt="Kotlin/Java">
    </a>
    <a href="https://twitter.com/codevapor">
        <img src="https://img.shields.io/badge/web-digi.me-red.svg" alt="Web">
    </a>
</p>

<br>

## Introduction

The digi.me private sharing platform empowers developers to make use of user data from thousands of sources in a way that fully respects a user's privacy, and whilst conforming to GDPR. Our consent driven solution allows you to define exactly what terms you want data by, and the user to see these completely transparently, allowing them to make an informed choice as to whether to grant consent or not.

## Requirements

### Development
- Android Studio 3.0 or newer.
- Gradle 5.0 or newer.
- Kotlin 1.30 or newer. **\***

**\*** The SDK is written entirely in Kotlin, but is compatible with Java projects.

### Deployment
- Android 5.1 or newer (API Level 21).

## Installation

### Gradle/Maven

1. Add the digi.me repository to your root `build.gradle` file:

	`maven { url https://repository.sysdigi.me/m2/libs-release }`
	
2. Include the digi.me SDK as a dependency in your app `build.gradle` file:

	`implementation "me.digi:sdk:1.0.0"`

### Manual

1. Download the source code for the SDK.
2. In Android Studio, import the SDK as a module.
3. In your app `build.gradle`, include the module as a dependency:

	`implementation project(":sdk")`
	
## Getting Started - 5 Simple Steps!

We have taken the most common use case for the digi.me Private Sharing SDK and compiled a quick start guide, which you can find below. Nonetheless, we implore you to [explore the documentation further](docs).

This example will show you how to configure the SDK, and get you up and running with **retrieving user data**.

### 1. Obtaining your Contract ID, Application ID & Private Key:

To access the digi.me platform, you need to obtain an `AppID` for your application. You can get yours by filling out the registration form [here](https://go.digi.me/developers/register).

In a production environment, you will also be required to obtain your own `Contract ID` and `Private Key` from digi.me support. However, for sandbox purposes, we provide the following example values:

**Example Contract ID:** `fJI8P5Z4cIhP3HawlXVvxWBrbyj5QkTF `
<br>
**Example Private Key:**
	<br>&nbsp;&nbsp;&nbsp;&nbsp;Download: [P12 Key Store]()
	<br>&nbsp;&nbsp;&nbsp;&nbsp;Password: `monkey periscope`
	
You should include the P12 file in your project assets folder.

### 2. Configuring Callback Forwarding:

Because the digi.me Private Sharing SDK communicates with the digi.me app, you are required to forward invocations of `onActivityResult` through to the SDK so that it may process responses. In any activity that will be resposible for invoking methods on the SDK, override `onActivityResult` as below:

```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	super.onActivityResult(requestCode, responseCode, data)
	DMEAppCommunicator.getSharedInstance().onActivityResult(requestCode, responseCode, data)
}
```

### 3. Configuring the `DMEPullClient` object:
`DMEPullClient` is the object you will primarily interface with to use the SDK. It is instantiated with a context, and a `DMEPullConfiguration` object. **The provided context should always be the main application context.**

The `DMEPullConfiguration` object is instantiated with your `AppID`, `Contract ID` and `Private Key` in hex format. We provide a convenience method to extract the private key. The below code snippet shows you how to combine all this to get a configured `DMEPullClient`:

```kotlin
val privateKeyHex = DMECryptoUtilities(applicationContext).privateKeyHexFrom("p12-filename", "p12-password")
val configuration = DMEPullConfiguration("app-id", "contract-id", privateKeyHex)
val pullClient = DMEPullClient(applicationContext, configuration)
```

### 4. Requesting Consent:

Before you can access a user's data, you must obtain their consent. This is achieved by calling `authorize` on your client object:

```kotlin
pullClient.authorize(this) { session, error ->

}
```
*NB: `this` represents the activity which is setup to forward `onActivityResult`, as above.*

If a user grants consent, a session will be created and returned; this is used by subsequent calls to get data. If the user denies consent, an error stating this is returned. See [Handling Errors](#).

### 5. Fetching Data:

Once you have a session, you can request data. We strive to make this as simple as possible, so expose a single method to do so: 

```kotlin
pullClient.getSessionData() { files, error ->
	val jsonData = file?.fileContentAsJSON()
}
```

If successful, you will be returned a `DMEFile` object. It's `content` property is a byte array of the file data. In the case that the session obtained above isn't valid (it may have expired, for example), you will receive an error. In such cases, you should call `authorize` again to obtain a new session. See [Handling Errors](#).

`fileContentAsJSON` attempts to decode the binary file into a JSON map, so that you can easily extract the values you need to power your app.

## Contributions

digi.me prides itself in offering our SDKs completely open source, under the [Apache 2.0 Licence](LICENCE); we welcome contributions from all developers.

We ask that when contributing, you ensure your changes meet our [contribution guidelines]() before submitting a pull request.

## Further Reading

The topics discussed under [Quick Start]() are just a small part of the power digi.me Private Sharing gives to data consumers such as yourself. We highly encourage you to explore the [documentation]() for more in-depth examples and guides, as well as troubleshooting advice and showcases of the plethora of capabilities on offer.

Additionally, there are a number of example apps built on digi.me in the examples folder. Feel free to have a look at those to get an insight into the power of Private Sharing.
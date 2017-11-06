# Pixelr
This project showcases few of the usages of Firebase products (Realtime database, storage, authentication (google and twitter), cloud messaging, cloud functions, Remote config, Dynamic links etc.)

## setup

Follow the instructions below to setup your machine before you use this project

### Install / upgrade to latest version of android studio

- To install android studio go to https://developer.android.com/studio/index.html and download android studio for computer's operating system. Android studio is available for Mac, Windows and Linux.
- If you already have android studio installed click on "check for updates" from toolbar

  - Mac: Android studio -> Check for updates...

  - Windows: Help -> Check for update

### Upgrade to latest version of SDK, SDK Build tools and platform tools

- Once android studio is up to date (android studio 3.0+), open sdk manager and install latest vresion of sdk and build tools
  - Open sdk manager from toolbar or from **Tools -> Android -> SDK Manager**.

Description | Screentshot                                                         
--- | ---                                                          
Make sure Android API 27 is checked, if not select it and click OK | <img src="/images/api27.png" width="100%">
From SDK Tools tab, install Build-Tools 27 or above and Android SDK platform tools 26.0.1+ | <img src="/images/buildtools27.png" width="100%">
Install latest version of google repository (58+), from tools tab | <img src="/images/googlerepo58.png" width="100%">
Restart android studio, and make sure Firebase is visible under tools | <img src="/images/toolsfirebase.png" width="100%">

  
### Install firebase command line tools (firebase CLI)

- You will need to install node js. Make sure npm (node package manager is installed). To check run `npm --version` command in terminal. It will show npm version if installed or it will say command not found if not installed. You can install node js and npm from here https://nodejs.org/en/download/

- `npm -version` should work after this. Run this command to install Firebase CLI `npm install -g firebase-tools`. This installs firebase tools globally in your machine.

- To check if firebase is installed run `firebase --version` in terminal. It should show `3.14.0` or higher. The setup is complete we can now start with the sample project (Pixelr).


## Getting the sample app

Checkout the sample app:

`git clone https://github.com/drulabs/Pixelr.git` then switch to branch starter

Or download as zip https://github.com/drulabs/Pixelr/archive/starter.zip
  

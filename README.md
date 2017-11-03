# Pixelr
This project showcases few of the usages of Firebase products (Realtime database, storage, authentication (google and twitter), cloud messaging, cloud functions, Remote config, Dynamic links etc.)

## setup

Follow the instructions below to setup your machine before you use this project

### Install / upgrade to latest version of android studio

- To install android studio go to https://developer.android.com/studio/index.html and download android studio for computer's operating system. Android studio is available for Mac, Windows and Linux.
- If you already have android studio installed click on "check for updates" from toolbar

  - Mac: Android studio -> Check for udpates...

  - Windows: Help -> Check for update

### Upgrade to latest version on SDK and SDK Build tools

- Once android studio is up to date (android studio 3.0+), open sdk manager and install latest vresion of sdk and build tools
  - Open sdk manager from toolbar or from **Tools -> Android -> SDK Manager**.

Description | Screentshot                                                         
--- | ---                                                          
Make sure Android API 27 is checked, if not select it and click OK | <img src="/images/api27.jpg" width="70%">
From SDK Tools tab, install Build-Tools 27 or above | <img src="/images/buildtools27.jpg" width="70%">
Restart android studio, and make sure Firebase is visible under tools | <img src="/images/toolsfirebase.jpg" width="70%">
--- | ---
  
### Install firebase command line tools (firebase CLI)

- You will need to install node js. Make sure npm (node package manager is installed). To check run `npm --version` command in terminal. It will show npm version if installed or it will say command not found if not installed. You can install node js and npm from here https://nodejs.org/en/download/

- `npm -version` should work after this. Run this command to install Firebase CLI `npm install -g firebase-tools`. This installs firebase tools globally in your machine.

- To check if firebase is installed run `firebase --version` in terminal. It should show `3.14.0` or higher. The setup is done we can now start the sample Pixelr project.

# SMAPI-Android-Installer
Installs SMAPI to Android Devices

# For Players
## How to use
1. Download the latest apk from the releases tab, it contains all the latest files.
2. After installing the SMAPI Installer app, open it up and click the `Install` button to begin the install process.
3. The process may take some time depending on the age and specs of the device.
-Samsung Galaxy S10 took 40s.
-Samsung Galaxy S6 took 1m 58s.
4. Once it's installed, you're good to go and can uninstall the Installer app if you please to do so.
5. Add mods in the newly created `StardewValley/Mods` directory.
6. Lastly, Just click on the `SMAPI Stardew Valley` icon to start the SMAPI version of the game.

## Mod Support
The focus when it comes to mods is try to make the bigger mods compatible first!

Here's a list of some of the bigger mods that are supported:

- [Content Patcher](https://www.nexusmods.com/stardewvalley/mods/1915) by Pathoschild.
- [Json Assets](https://www.nexusmods.com/stardewvalley/mods/1720) by spacechase0.
- [PyTK](https://www.nexusmods.com/stardewvalley/mods/1726) by Platonymous (aka Routine).
- [Partial SVE support](https://www.nexusmods.com/stardewvalley/mods/3753) by FlashShifter.
- [Farm Type Manager](https://www.nexusmods.com/stardewvalley/mods/3231) by EscaMMC (aka Esca).
- And plenty more!

## Manual Install
If the Automatic Installer doesn't work I'll always have a manual install option under the releases tab.

Note: It's the `.zip` file.

## How the installer works
1. It pulls the actual games apk from the device.
2. It creates all the needed directories on the the device.
3. Modifies the apk to add SMAPI needed files.
4. Signs the modified apk using JAR signing.
5. Lastly, it installs the new version.

## TODO
- Faster modify times

## APK Signing Details
![alt text](https://github.com/MartyrPher/SMAPI-Android-Installer/blob/master/current_scheme.PNG)

This Test was ran with Google's apksigner program.

It currently supports v1 scheme for apks.

It uses the debug.keystore provided by google to sign it.
<p class='logo'>
  <img alt="logo" src="https://raw.githubusercontent.com/rahul-jha98/PasswordKeeper/image_assets/Logo.svg" width="480">
</p>

> Securely store your passwords in your Google Drive.

**🚀 Checkout the website live at** : https://thepasswordkeeper.netlify.app/

Password Keeper is a web app that can be used to store your passwords or other account details in your Google Drive. The data is store in an encrypted format and can only be decrypted using a master password that you set during first login.

## 🤔 Why Google Sheet?
For storing all the encrypted passwords a Google Sheet stored in your Google Drive is used as a database. The reason for choosing this approach is that since the Google Sheet will only store the passwords of a singe person and the number of passwords a will hopefully never be more than hundred. Thus, there is not much performace harm in using Google Sheet to store data. 
But the benefit of using Google Sheet is that it completely removes the need for a backend. Since, Google provides REST API to manipulate Google Sheet the web-app could directly interact with data stored in Google Sheet.

## ⚡ Features
With so many password manager already existing, one might wonder why should anyone choose PasswordKeeper. Few of the reasons is mentioned below - 
- **No Backend** - PasswordKeeper is a standalone web app without any backend server. The data you enter is saved directly in your Google Drive. 
- **Web Based** - While there are many password managers that do not have a backend but rather store the passwords in the user's mobile device itself. The limitation of this approach is that if you don't have your mobile near you, you cannot access your passwords.
- **Free to use**
- **Open Source** - The complete source code of PasswordKeeper is available on Github thus you can verify that there is nothing suspicious happening and also help make PasswordKeeper better for everyone.

## 👀 How it looks

<h4>Main Screen</h4>
<img width="720px" src="https://raw.githubusercontent.com/rahul-jha98/PasswordKeeper/image_assets/Home.png" />

<h4>Detail Screen</h4>
<img width="720px" src="https://raw.githubusercontent.com/rahul-jha98/PasswordKeeper/image_assets/Detail.png" />

<h4>How it looks in Sheet</h4>
<img width="720px" src="https://raw.githubusercontent.com/rahul-jha98/PasswordKeeper/image_assets/Sheet.png" />

<h4>Add Categories with Custom Fields</h4>
<img width="720px" src="https://raw.githubusercontent.com/rahul-jha98/PasswordKeeper/image_assets/Category.png" />

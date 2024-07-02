# Wordle UA Game
## What is the project about?

Wordle UA is a browser-based intellectual game where the player has to guess the word that his opponent guessed in real time. The game helps to develop language skills, improve cognitive abilities and expand vocabulary.

## Features

- **Multiplayer game:** Guess the words riddled by your opponent in real time.
- **Different levels of difficulty:** Suitable for a wide audience of all ages and language levels.
- **Adaptive interface:** Easy to use on different devices, including mobile phones and tablets.
- **Hints function:** Helps to get additional information about the puzzled word in case of difficulties.
- **Social aspect:** Ability to compete with friends or other users online.

### Installation Instruction

#### How to download project on my local machine?

For downloading the project locally you can use two variants:

1. Download the ZIP archive from the repository page.

   The method is easy, the next steps helps you:

    1. Find the button `Code` and press it.
    2. Find the button `Download ZIP` and press it. The downloading must start.
    3. Unzip the archive in soe directory and run the IDEA in this directory.

   Project has been installed.


2. Use the `Git` for downloading the repository locally.

    1. Enter your [name][1], [email][2] of GitHub account locally on your machine.
    2. Create an empty directory and initialize it as git repository. Use the next
       command - `git init`.
    3. Adds this repository to yours with name `origin` (you can change it, if you want):

       ```
       $ git remote add origin git@github.com:wordleUA/wordle.git
       ```
       But you need configure your SSH connection to your GitHub profile in Git Bash.
       For viewing that the repository has been added successfully to your local
       repository, you need execute the next command and get the following result:

       ```
       $ git remote -v
       ```

       After this step your local repository has got a 'connection' to the remote project from the GitHub repository.

    4. For downloading the project use the following command:

       ```
       $ git pull origin main
       ```

       After these steps your project directory must contain the project files from
       GitHub repository.

### Run manually

##### Backend Server configuration

For running the code you needn't install server and configure it, because the Spring Boot starter
makes it instead of you.

For running the app on the server you need only run the main method in Java class.

##### Frontend Server configuration

For running the code you need install NPM, Node. Then run next command
`npm install`.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management Tool

## Authors

* **Oleksandr Klymenko** - *Backend Developer* - [kllymenko](https://github.com/kllymenko)
* **Oksana Vykhryst** - *Backend Developer* - [vykhryst](https://github.com/vykhryst)
* **Oryna Kasapova** - *Backend Developer* - [OrynaK](https://github.com/OrynaK)
* **Sofia Kolokolcheva** - *Frontend Developer* - [Sofikoshka](https://github.com/Sofikoshka)

[1]:https://docs.github.com/en/get-started/getting-started-with-git/setting-your-username-in-git
[2]:https://docs.github.com/en/account-and-profile/setting-up-and-managing-your-personal-account-on-github/managing-email-preferences/setting-your-commit-email-address

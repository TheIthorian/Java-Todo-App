<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

<h3 align="center">Java To-do App</h3>
<div>
  <p align="center">
    A simple command line interface for a to-do app, made to explore writing Java
    <br />
    <a href="https://github.com/TheIthorian/Java-Todo-App"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/TheIthorian/Java-Todo-App/issues">Report Bug</a>
    ·
    <a href="https://github.com/TheIthorian/Java-Todo-App/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->

## About The Project

<!-- [![Java To-do App Screen Shot][product-screenshot]](https://example.com) -->

I have some experience with Apex but I wanted to explore Java so I wrote a simple command line interface for a to-do app.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

[![Java][java.com]][java-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->

## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

-   java: https://www.java.com/en/download/

### Installation

1. Clone the repo
    ```sh
    git clone https://github.com/TheIthorian/Java-Todo-App.git
    ```

---

### Compiling

Compile classes

```sh
mvn compile
```

Create Todo.jar from class (Java Archive)

```sh
mvn clean package
# Or to include dependencies:
mvn assembly:assembly -DdescriptorId=jar-with-dependencies
```

Run .jar

```sh
java -jar target/todo-0.0.1.jar Todo
```

Read more here [https://www.cs.odu.edu/~zeil/cs382/latest/Public/runAnywhere/index.html](https://www.cs.odu.edu/~zeil/cs382/latest/Public/runAnywhere/index.html)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

---

<!-- USAGE EXAMPLES -->

## Usage

-   Set your username and password
    ```sh
    java -jar todo-0.0.1.jar -cf -username=<my_username> -password=<my_password>
    ```
-   Add the user

    ```sh
    java -jar todo-0.0.1.jar -addUser
    ```

-   Add a todo item

    ```sh
    java -jar todo-0.0.1.jar -a title="Some title" description="some description"
    ```

-   Get all items
    ```sh
    java -jar todo-0.0.1.jar -g
    ```

_For more examples, please refer to the [Documentation](https://github.com/TheIthorian/Java-Todo-App)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ROADMAP -->

## Roadmap

-   [ ] Link to Google Calendar

See the [open issues](https://github.com/TheIthorian/Java-Todo-App/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->

## Contact

Project Link: [https://github.com/TheIthorian/Java-Todo-App](https://github.com/TheIthorian/Java-Todo-App)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/TheIthorian/Java-Todo-App.svg?style=for-the-badge
[contributors-url]: https://github.com/TheIthorian/Java-Todo-App/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/TheIthorian/Java-Todo-App.svg?style=for-the-badge
[forks-url]: https://github.com/TheIthorian/Java-Todo-App/network/members
[stars-shield]: https://img.shields.io/github/stars/TheIthorian/Java-Todo-App.svg?style=for-the-badge
[stars-url]: https://github.com/TheIthorian/Java-Todo-App/stargazers
[issues-shield]: https://img.shields.io/github/issues/TheIthorian/Java-Todo-App.svg?style=for-the-badge
[issues-url]: https://github.com/TheIthorian/Java-Todo-App/issues
[license-shield]: https://img.shields.io/github/license/TheIthorian/Java-Todo-App.svg?style=for-the-badge
[license-url]: https://github.com/TheIthorian/Java-Todo-App/blob/master/LICENSE.txt
[product-screenshot]: images/screenshot.png
[java.com]: https://img.shields.io/badge/Java-0769AD?style=for-the-badge&logo=java&logoColor=white
[java-url]: https://www.java.com/en/

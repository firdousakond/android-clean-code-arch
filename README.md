## Android Clean Architecture
 
## What is Clean Architecture?

Clean architecture is a method of software development in which you should be able to identify what a program performs merely by looking at its source code. Robert C. Martin, also known as Uncle Bob, came up with the Clean Architecture concept in the year 2012.
 
 ![clean-arch](https://user-images.githubusercontent.com/19757294/176735178-b9d62f4d-ab6d-402d-99d2-d205daac18a6.jpeg)

 
## Why Clean Architecture?
1. Separation of Concerns — Separation of code in different modules or sections with specific responsibilities making it easier for maintenance and further modification.
2. Loose coupling — flexible code anything can be easily be changed without changing the system
3. Easily Testable

## Layers of clean architecture

**Presentation or UI:** A layer that interacts with the UI, mainly Android Stuff like Activities, Fragments, ViewModel, etc. It is dependent on Use Cases.</br>
**Domain:** Contains the business logic of the application. It is the individual and innermost module.</br>
**Data:** It includes the domain layer. It would implement the interface exposed by domain layer and dispenses data to app



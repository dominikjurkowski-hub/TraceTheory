# TraceTheory – Foata Normal Form (FNF) Generator

## Introduction

The **TraceTheory** project is an implementation of algorithms from **Trace Theory**, which is used for modeling concurrent (parallel) systems using trace monoids (free partially commutative monoids).

The main goal of the application is to calculate the **Foata Normal Form (FNF)** for a given trace (word) and generate the corresponding **Foata Dependence Graph** in its minimal form.

## Key Features

* **Transaction and Variable Definition:** 
* **Dependence Relation Calculation (Dependencies):**
* **Foata Normal Form (FNF):**
* **Dependence Graph Generation:**
* **Graph Optimization:**

## Technology Stack

* **Language:** Java
* **Build System:** Gradle
* **Output Format:** DOT (for graph visualization using tools like Graphviz)

## How to Run

The project is configured using Gradle.

1.  **Clone the repository:**
    ```bash
    git clone [LINK_TO_YOUR_REPOSITORY]
    cd TraceTheory
    ```
2.  **Run the application:**
    Executing the main class (`Main.java`) processes the sample input files from the `input` directory and saves the resulting graphs to the `output` directory.
    ```bash
    ./gradlew run
    ```
3.  **Visualize the Graph (Optional):**
    The resulting `.dot` files can be compiled into graphical formats (PNG, SVG) using the Graphviz tool (requires installation).
    ```bash
    dot -Tpng output/case2.dot -o case2_graph.png
    ```

***

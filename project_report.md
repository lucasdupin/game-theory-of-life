# Game Theory of Live
## Machine Learning Engineer Nanodegree
Lucas Dupin  
August 29th, 2016

## I. Definition

### Project Overview
Game Theory of Live, is an ode to [Game of Live](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) with a machine learning twist.

The Game of Life is a mathematical experiment, built in 1970 by [John Horton Conway](https://en.wikipedia.org/wiki/John_Horton_Conway). At the time Conway was interested in simplifying a problem solved by John von Neumann, in the 1940s - find a mathematical model for a machine that could create copies of itself -. It simulates cellular growth on a 2D grid, is capable of generating complex patterns, and is also considered an universal Turing Machine.

This game has no players and the only decision the user makes is the initial layouts of the cells, after that they live on their own, following a simple set of rules:

1. Any live cell with fewer than two live neighbors dies, as if caused by under-population.
1. Any live cell with two or three live neighbors lives on to the next generation.
1. Any live cell with more than three live neighbors dies, as if by over-population.
1. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

These are examples of patterns that can be formed by the original game:

![Game of life](https://upload.wikimedia.org/wikipedia/commons/e/e5/Gospers_glider_gun.gif)

This project will breath fresh air into this problem. Also using a small set of rules, we are building a cell automata that isn't passive to its environment anymore, but learns how it behaves, just by existing, and does what we do best: live and reproduce.

### Problem Statement
It's clear that the Game of Live succeed on its goals of generating complex patters and emerging self organization, but it doesn't grasp the most intricate aspects of life, such as evolution and free will. It's complicate to say that a being will behave exactly the same over and over, without ever learning what hurts or pleases him.

Having this in mind, more existential problems were given. These are the rules that guide the life of a being:

1. Any live cell needs to eat, or it will starve to death
1. Any live cell wants to reproduce, and perpetuate its species
1. Eventually a cell ages and dies
1. Mutations occur randomly, creating new kinds of cells

Minor assumptions made considering these rules:

* Mutating means gaining or loosing a leg - and a degree of mutation/similarity compares number of legs
* An user drops food whenever he interacts with the app
* Simpler cells are considered food by their evolved counterparts
* The bigger a cell is, the more it has to eat
* An older cell moves slower

Actions that a being can execute:

* Moving each leg to a chosen direction (top, right, bottom, left)
* Eating
* Reproducing

This problem will be solved using **Reinforcement Learning** and **Game Theory**. Each species will have its own **Q-Table**, inherited from its parent species, and will learn how to behave by following [Bell's equation](https://en.wikipedia.org/wiki/Bellman_equation) and evaluating a **Quality** function. The world starts over from scratch whenever all life is extinguished.

Each action results in an reward, negative or positive, depending on whether you ate, reproduced, were eaten or just aged.

Since we are also targeting phones, memory usage is a concern, so real numbers will be grouped into bigger bins, to save precious RAM while also speeding up training.

Over time, all beings will learn what to do to maximize their lifespan, and guarantee reproduction. This injects another dimension of intelligence into the original Game of Live.

### Metrics
Besides being an art project, goals can be well defined, not only by letting the piece evolve by itself, but also by trying to mimic behaviors found in every life form.

The main metrics we'll observe will be:

* Cell score after death - based on rewards after each iteration
* Lifespan - if it could age, died by starvation or were eaten
* Capacity to reproduce - it's crucial for a species to be able to perpetuate

It's expected that scores will be lower initially, since cells won't have learned yet that they have to eat and reproduce. And will increase over time, as cells evolve.

There is one behavior that's still part of this research and won't be considered success or failure. This behavior is how far a cell can evolve and still keep the symbiosis of the environment.

## II. Analysis
_(approx. 2-4 pages)_

### Data Exploration
In this section, you will be expected to analyze the data you are using for the problem. This data can either be in the form of a dataset (or datasets), input data (or input files), or even an environment. The type of data should be thoroughly described and, if possible, have basic statistics and information presented (such as discussion of input features or defining characteristics about the input or environment). Any abnormalities or interesting qualities about the data that may need to be addressed have been identified (such as features that need to be transformed or the possibility of outliers). Questions to ask yourself when writing this section:
- _If a dataset is present for this problem, have you thoroughly discussed certain features about the dataset? Has a data sample been provided to the reader?_
- _If a dataset is present for this problem, are statistics about the dataset calculated and reported? Have any relevant results from this calculation been discussed?_
- _If a dataset is **not** present for this problem, has discussion been made about the input space or input data for your problem?_
- _Are there any abnormalities or characteristics about the input space or dataset that need to be addressed? (categorical variables, missing values, outliers, etc.)_

### Exploratory Visualization
In this section, you will need to provide some form of visualization that summarizes or extracts a relevant characteristic or feature about the data. The visualization should adequately support the data being used. Discuss why this visualization was chosen and how it is relevant. Questions to ask yourself when writing this section:
- _Have you visualized a relevant characteristic or feature about the dataset or input data?_
- _Is the visualization thoroughly analyzed and discussed?_
- _If a plot is provided, are the axes, title, and datum clearly defined?_

### Algorithms and Techniques
In this section, you will need to discuss the algorithms and techniques you intend to use for solving the problem. You should justify the use of each one based on the characteristics of the problem and the problem domain. Questions to ask yourself when writing this section:
- _Are the algorithms you will use, including any default variables/parameters in the project clearly defined?_
- _Are the techniques to be used thoroughly discussed and justified?_
- _Is it made clear how the input data or datasets will be handled by the algorithms and techniques chosen?_

### Benchmark
In this section, you will need to provide a clearly defined benchmark result or threshold for comparing across performances obtained by your solution. The reasoning behind the benchmark (in the case where it is not an established result) should be discussed. Questions to ask yourself when writing this section:
- _Has some result or value been provided that acts as a benchmark for measuring performance?_
- _Is it clear how this result or value was obtained (whether by data or by hypothesis)?_


## III. Methodology
_(approx. 3-5 pages)_

### Data Preprocessing
In this section, all of your preprocessing steps will need to be clearly documented, if any were necessary. From the previous section, any of the abnormalities or characteristics that you identified about the dataset will be addressed and corrected here. Questions to ask yourself when writing this section:
- _If the algorithms chosen require preprocessing steps like feature selection or feature transformations, have they been properly documented?_
- _Based on the **Data Exploration** section, if there were abnormalities or characteristics that needed to be addressed, have they been properly corrected?_
- _If no preprocessing is needed, has it been made clear why?_

### Implementation
In this section, the process for which metrics, algorithms, and techniques that you implemented for the given data will need to be clearly documented. It should be abundantly clear how the implementation was carried out, and discussion should be made regarding any complications that occurred during this process. Questions to ask yourself when writing this section:
- _Is it made clear how the algorithms and techniques were implemented with the given datasets or input data?_
- _Were there any complications with the original metrics or techniques that required changing prior to acquiring a solution?_
- _Was there any part of the coding process (e.g., writing complicated functions) that should be documented?_

### Refinement
In this section, you will need to discuss the process of improvement you made upon the algorithms and techniques you used in your implementation. For example, adjusting parameters for certain models to acquire improved solutions would fall under the refinement category. Your initial and final solutions should be reported, as well as any significant intermediate results as necessary. Questions to ask yourself when writing this section:
- _Has an initial solution been found and clearly reported?_
- _Is the process of improvement clearly documented, such as what techniques were used?_
- _Are intermediate and final solutions clearly reported as the process is improved?_


## IV. Results
_(approx. 2-3 pages)_

### Model Evaluation and Validation
In this section, the final model and any supporting qualities should be evaluated in detail. It should be clear how the final model was derived and why this model was chosen. In addition, some type of analysis should be used to validate the robustness of this model and its solution, such as manipulating the input data or environment to see how the model’s solution is affected (this is called sensitivity analysis). Questions to ask yourself when writing this section:
- _Is the final model reasonable and aligning with solution expectations? Are the final parameters of the model appropriate?_
- _Has the final model been tested with various inputs to evaluate whether the model generalizes well to unseen data?_
- _Is the model robust enough for the problem? Do small perturbations (changes) in training data or the input space greatly affect the results?_
- _Can results found from the model be trusted?_

### Justification
In this section, your model’s final solution and its results should be compared to the benchmark you established earlier in the project using some type of statistical analysis. You should also justify whether these results and the solution are significant enough to have solved the problem posed in the project. Questions to ask yourself when writing this section:
- _Are the final results found stronger than the benchmark result reported earlier?_
- _Have you thoroughly analyzed and discussed the final solution?_
- _Is the final solution significant enough to have solved the problem?_


## V. Conclusion
_(approx. 1-2 pages)_

### Free-Form Visualization
In this section, you will need to provide some form of visualization that emphasizes an important quality about the project. It is much more free-form, but should reasonably support a significant result or characteristic about the problem that you want to discuss. Questions to ask yourself when writing this section:
- _Have you visualized a relevant or important quality about the problem, dataset, input data, or results?_
- _Is the visualization thoroughly analyzed and discussed?_
- _If a plot is provided, are the axes, title, and datum clearly defined?_

### Reflection
In this section, you will summarize the entire end-to-end problem solution and discuss one or two particular aspects of the project you found interesting or difficult. You are expected to reflect on the project as a whole to show that you have a firm understanding of the entire process employed in your work. Questions to ask yourself when writing this section:
- _Have you thoroughly summarized the entire process you used for this project?_
- _Were there any interesting aspects of the project?_
- _Were there any difficult aspects of the project?_
- _Does the final model and solution fit your expectations for the problem, and should it be used in a general setting to solve these types of problems?_

### Improvement
In this section, you will need to provide discussion as to how one aspect of the implementation you designed could be improved. As an example, consider ways your implementation can be made more general, and what would need to be modified. You do not need to make this improvement, but the potential solutions resulting from these changes are considered and compared/contrasted to your current solution. Questions to ask yourself when writing this section:
- _Are there further improvements that could be made on the algorithms or techniques you used in this project?_
- _Were there algorithms or techniques you researched that you did not know how to implement, but would consider using if you knew how?_
- _If you used your final solution as the new benchmark, do you think an even better solution exists?_

-----------

**Before submitting, ask yourself. . .**

- Does the project report you’ve written follow a well-organized structure similar to that of the project template?
- Is each section (particularly **Analysis** and **Methodology**) written in a clear, concise and specific fashion? Are there any ambiguous terms or phrases that need clarification?
- Would the intended audience of your project be able to understand your analysis, methods, and results?
- Have you properly proof-read your project report to assure there are minimal grammatical and spelling mistakes?
- Are all the resources used for this project correctly cited and referenced?
- Is the code that implements your solution easily readable and properly commented?
- Does the code execute without error and produce results similar to those reported?

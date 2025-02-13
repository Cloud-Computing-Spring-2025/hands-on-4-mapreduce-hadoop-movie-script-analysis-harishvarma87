
# Movie Script Analysis 


## Project Overview

This project analyzes movie scripts using Apache Hadoop’s MapReduce framework to extract useful insights. The analysis includes:

Most Frequent Words by Character – Identifying the most used words by each character.
Dialogue Length Analysis – Measuring the total length of dialogues spoken by each character.
Unique Words by Character – Listing unique words spoken by each character.

## Approach and Implementation

###### Most Frequent Words by Character

Mapper (CharacterWordMapper)

Splits each line into character name and dialogue text.
Tokenizes the dialogue and removes non-alphabetic characters.
Emits each (word, 1) pair.
Reducer (CharacterWordReducer)

Aggregates word counts and outputs (word, total occurrences).

###### Dialogue Length Analysis

Mapper (DialogueLengthMapper)

Splits the line into character name and dialogue.
Calculates length of the dialogue.
Emits (character, dialogue_length).
Reducer (DialogueLengthReducer)

Sums up dialogue lengths per character.
Outputs (character, total dialogue length).


###### Unique Words by Character

Mapper (UniqueWordsMapper)

Extracts unique words from the dialogue.
Emits (character, word) for each unique word.
Reducer (UniqueWordsReducer)

Collects all unique words per character.
Outputs (character, set of unique words).


###### Unique Words by Character

Mapper (UniqueWordsMapper)

Extracts unique words from the dialogue.
Emits (character, word) for each unique word.
Reducer (UniqueWordsReducer)

Collects all unique words per character.
Outputs (character, set of unique words).


## Execution Steps

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn clean install
```

### 3. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp target/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 4. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/movie_dialogues.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

### 6. **Navigate to the Hadoop directory**

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/movie_scripts
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put movie_dialogues.txt /input/movie_scripts/
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command:

```bash
hadoop jar hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.movie.script.analysis.MovieScriptAnalysis /input/movie_scripts/movie_dialogues.txt /output
```
After executing this, you will see the output related to counters.

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -cat /output/task1/part-r-00000
```

```bash
hadoop fs -cat /output/task2/part-r-00000
```

```bash
hadoop fs -cat /output/task3/part-r-00000
```

### 10. **Copy Output from HDFS to Local OS**

To copy the output from HDFS to your local machine:

1. Use the following command to copy from HDFS:
    ```bash
    hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```

2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ shared-folder/output/
    ```
3. Commit and push to your repo so that we can able to see your output

## Challenges Faced & Solutions

##### 1. Challenge: 
The docker cp command might fail if the resourcemanager container is not running or the destination path is incorrect.
##### Solution:
Run docker ps to check if the resourcemanager container is active.
Ensure that the destination path exists inside the container (docker exec -it resourcemanager /bin/bash and ls /opt/hadoop-3.2.1/share/hadoop/mapreduce/).

##### 2. Challenge: 
Faced runtime error when output file already exists
##### Solution:
Runhadoop (fs -rm -r /output/task1)

##### 3. Challenge: 
Errors when creating HDFS directories or copying files, such as No such file or directory or Permission denied.
##### Solution:
Verify HDFS is running properly: hadoop fs -ls /.

##### 4. Challenge: 
Job execution fails due to missing JAR, incorrect class name, or insufficient cluster resources.
##### Solution:
Ensure the JAR file is copied to the correct location (ls /opt/hadoop-3.2.1/share/hadoop/mapreduce/).

    
#### Sample output
Input Format

A movie script dialogue dataset where each line follows the format:

JACK: The ship is sinking! We have to go now.
ROSE: I won’t leave without you.
JACK: We don’t have time, Rose!

Expected Output
1. Most Frequently Spoken Words by Characters
the 3
we 3
have 2
to 2
now 1
without 1
2. Total Dialogue Length per Character
JACK 54
ROSE 25
3. Unique Words Used by Each Character
JACK [the, ship, is, sinking, we, have, to, go, now, don’t, time, rose]
ROSE [i, won’t, leave, without, you]



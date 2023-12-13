package com.example.pavesehunt.common

import com.example.pavesehunt.data.models.Question

object Questions {
    val questions = listOf(
        Question("What is the capital of France?", listOf("Berlin", "Paris", "Madrid", "Rome"), 1),
        Question("Which programming language is this code written in?", listOf("Java", "Kotlin", "Python", "C++"), 1),
        Question("What is the largest planet in our solar system?", listOf("Mars", "Jupiter", "Venus", "Saturn"), 1),
        Question("What is the largest ocean on Earth?", listOf("Atlantic Ocean", "Indian Ocean", "Southern Ocean", "Pacific Ocean"), 3),
        Question("Who wrote 'Romeo and Juliet'?", listOf("Charles Dickens", "William Shakespeare", "Jane Austen", "Mark Twain"), 1),
        Question("What is the capital of Japan?", listOf("Seoul", "Beijing", "Tokyo", "Bangkok"), 2),
        Question("Which planet is known as the 'Red Planet'?", listOf("Mars", "Jupiter", "Venus", "Saturn"), 0),
        Question("What is the currency of Brazil?", listOf("Peso", "Dollar", "Real", "Euro"), 2),
        Question("Who is the author of 'To Kill a Mockingbird'?", listOf("J.K. Rowling", "Harper Lee", "Ernest Hemingway", "George Orwell"), 1),
        Question("In which year did World War II end?", listOf("1943", "1945", "1950", "1939"), 1),
        Question("What is the largest mammal on Earth?", listOf("Elephant", "Blue Whale", "Giraffe", "Hippopotamus"), 1),
        Question("Which country is known as the 'Land of the Rising Sun'?", listOf("China", "Japan", "South Korea", "Vietnam"), 1),
        Question("Who painted the Mona Lisa?", listOf("Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Claude Monet"), 1),
        Question("What is the speed of light?", listOf("299,792 km/s", "150,000 km/s", "500,000 km/s", "200,000 km/s"), 0),
        Question("Which planet is closest to the Sun?", listOf("Venus", "Mercury", "Mars", "Earth"), 1),
        Question("What is the largest bird in the world?", listOf("Penguin", "Ostrich", "Eagle", "Emu"), 1),
        Question("Who is the current President of the United States?", listOf("Barack Obama", "Joe Biden", "Donald Trump", "Hillary Clinton"), 1),
        Question("What is the square root of 64?", listOf("6", "7", "8", "9"), 2),
        Question("Which chemical element has the symbol 'O'?", listOf("Oxygen", "Gold", "Iron", "Silver"), 0),
        Question("What is the main ingredient in guacamole?", listOf("Tomato", "Onion", "Avocado", "Cilantro"), 2)
    )
}
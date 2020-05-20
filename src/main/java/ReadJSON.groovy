import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// Get the File content as Input Stream
InputStream is = getClass().getResourceAsStream("/books.json")

// Parse the Input Stream to Traversable Data
def data = new JsonSlurper().parseText(is.getText())

// Iterate over the Traversable Data
data.catalog.book.eachWithIndex { bk, idx ->
    println "$idx - $bk.title by $bk.author ( $bk.genre )"
}

// Filter on particular title
def myBook = data.catalog.book.find { it -> it.title == "XML Developer's Guide" }

// Print the result
println "myBook Title - $myBook.title"

// Print the complete JSON data
println JsonOutput.prettyPrint(JsonOutput.toJson(data))
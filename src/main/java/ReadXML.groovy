import groovy.xml.XmlUtil
// Get the File content as Input Stream
InputStream is = getClass().getResourceAsStream("/books.xml")

// Parse the Input Stream to Traversable Data
// Sample XML root element will be the first in the list "catalog"
def catalog = new XmlSlurper().parseText(is.getText())

// Iterate over the Traversable Data
catalog.book.eachWithIndex { bk, idx ->
    println "$idx - $bk.title by $bk.author ( $bk.genre )"
}

// Filter on particular title
def myBook = catalog.book.find { it -> it.title == "XML Developer's Guide" }

// Print the result
println "myBook Title - $myBook.title"

// Print the complete XML data
println XmlUtil.serialize(catalog)

// Read EDI file as InputStream
InputStream is = getClass().getResourceAsStream("/SampleX12.edi")

String data = is.getText()

String fieldSeparator, lineSeparator, ISA_ControlID = "", GS_ControlID = ""
if(data.startsWith("ISA")){
    fieldSeparator = data[3]
    println "fieldSeparator: $fieldSeparator"
    lineSeparator = data[105]
    println "lineSeparator: $lineSeparator"
    data.tokenize(lineSeparator).each { line ->
        if(line.startsWith("ISA")) {
            ISA_ControlID = line.tokenize(fieldSeparator).get(13)
        }else if(line.startsWith("GS")){
            GS_ControlID = line.tokenize(fieldSeparator).get(6)
        }
    }
}

println("ISA_ControlID: $ISA_ControlID")
println("GS_ControlID: $GS_ControlID")

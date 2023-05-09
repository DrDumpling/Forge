import environment.Interpreter
import parser.Parser
import parser.nodes.ASTNode
import parser.nodes.TreeBuilder
import tokens.Tokenizer
import java.io.BufferedReader
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime

lateinit var interpreter: Interpreter

fun main(args : Array<String>) {
    handleArgs(args)

    val bufferedReader: BufferedReader = File(args[0]).bufferedReader()
    val inputProgram = bufferedReader.use { it.readText() }

    val tokens = Tokenizer(inputProgram).tokenize()
    val evaluatedNodes =
        TreeBuilder(tokens.map { ASTNode(it, emptyList()) } as MutableList<ASTNode>).buildTrees()

    val statements = Parser.parseStatements(evaluatedNodes)
    interpreter = Interpreter(statements)
    println("Executed in " + TimeUnit.MILLISECONDS.convert(measureNanoTime {
        interpreter.execute()
    }, TimeUnit.NANOSECONDS) + " ms")

    println("final variable state:")
    println(interpreter.variableSpace.heldValues.map { x ->
        "(${x.key}, ${x.value.value})"
    })
}

fun handleArgs(args: Array<String>) {
    if (args.isEmpty()) {
        println("Provide an input file location")
        println("Example: java -jar forge.jar examples\\count.txt")
        exitProcess(0)
    }
    if (args.size > 1) {
        println("Expected only one input file")
        println("Example: java -jar forge.jar examples\\count.txt")
        exitProcess(0)
    }
    if (!File(args[0]).exists()) {
        println("File ${args[0]} does not exist, please provide a valid file.")
        exitProcess(0)
    }
}
import java.util.*

fun main(args: Array<String>) {
    println("Hello World!")
    //INMUTABLES (no se reasigna "=")
    val inmutable: String = "Alisson";
    //inmutable = "Alisson";
    //mutables (no se reasigna)
    var mutable: String ="Alisson";
    mutable = "Alisson";

    // val > var
    // Duck typing
    var ejemploVariable= "Alisson Curay"
    val edadEjemplo: Int = 15
    // ejemploVariable = edadEjemplo;

    // Variable primitiva
    val nombreEstudiante: String = "Alisson Curay"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'S'
    val mayorEdad: Boolean = true
    //Clases Java
    val fechaNacimiento: Date = Date()

    //SWITCH
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }
        "S" -> {
            println("Soltero")
        }
        else -> {
            println("No sabemos")
        }
    }
    val esSoltero = (estadoCivilWhen == "S")
    val coqueteo = if (esSoltero) "Si" else "No"
    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00, 20.00)
    calcularSueldo(10.00, bonoEspecial = 20.00) // parámetro nombrado
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00) // parámetros nombrados

    val sumaUno = Suma(1,1)
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
}
//void -> Unit
fun imprimirNombre(nombre: String): Unit {
    // "Nombre : " + nombre
    println("Nombre : ${nombre}")//template Strings
}
fun calcularSueldo(
    sueldo: Double, //requerido
    tasa: Double = 12.00, //Opcional (defecto)
    bonoEspecial: Double? = null, // Opcion null -> nullable, (puede ser un doble o nulo)
): Double{
    //Int -> Int? (nullable)
    //String -> String? (nullable)
    //Date -> Date? (nullable)
    if(bonoEspecial == null){
        return sueldo * (100/tasa)
    }else{
        return sueldo * (100/tasa) + bonoEspecial
    }
}

//clase abstracta para extender otras clases
abstract class NumerosJava{
    protected val numeroUno: Int
    private val numeroDos: Int

    constructor(
        uno:Int,
        dos: Int
    ){//Bloque de código del constructor
        this.numeroUno= uno
        this.numeroDos= dos
        println("Inicializando")
    }
}
abstract class Numeros(//Constructor primario
    // Ejemplo
    // uno: Int, (parametro sin modificar acceso)
    // private var uno: Int, // propiedad publica clase numeros.uno
    // var uno: Int, // propiedad de la clase (por defecto public)
    // public var uno: Int,
    protected val numeroUno: Int, // Propiedad de la clase protected numeros.numeroUno
    protected val numeroDos: Int, // Propiedad de la clase protected numeros.numeroDos
){
    // var cedula: string = " (public es por defecto)
    // private valorCalculado: Int = 0 (private)

    init { // bloque constructor primario
        this.numeroUno; this.numeroDos; // this es opcional
        numeroUno; numeroDos; // sin el "this" es lo mismo
        println("Inicializando")
    }
}
class Suma ( // Constructor primario suma
    unoParametro: Int, // Parametro
    dosParametro: Int, // Parametro
): Numeros(unoParametro, dosParametro){ //Extendiendo y mandando los parámetros (super)
    init { // Bloque codigo constructor primario
        this.numeroUno
        this.numeroDos
    }

    constructor( // Segundo constructor
        uno: Int?, // Parametros
        dos: Int // Parametros
    ):this (
        if(uno == null) 0 else uno,
        dos
    )

    constructor( // Tercer constructor
        uno: Int, // Parametros
        dos: Int? // Parametros
    ):this (
        uno,
        if(dos == null) 0 else dos,
    )
}
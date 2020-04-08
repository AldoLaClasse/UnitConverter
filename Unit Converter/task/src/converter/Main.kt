package converter

import java.util.Scanner

enum class MASSES (val short : String, val singular : String , val plural : String) {
    GRAM("g","gram","grams"),
    KILOGRAM("kg","kilogram", "kilograms"),
    MILLIGRAM("mg", "milligram", "milligrams"),
    POUND("lb","pound","pounds"),
    OUNCE("oz","ounce","ounces");
}

enum class LONGUEURS (val short : String, val singular : String , val plural : String) {
    METER("m", "meter", "meters"),
    KILOMETER("km","kilometer","kilometers"),
    MILLIMETER("mm","millimeter", "millimeters"),
    CENTIMETER("cm","centimeter","centimeters"),
    MILE("mi", "mile", "miles"),
    YARD("yd","yard","yards"),
    FOOT("ft","foot","feet"),
    INCH("in","inch","inches");
}

enum class TEMPERATURES (val short : String, val medium : String , val long : String) {
    CELSIUS("c","dc", "celsius"),
    FAHRENHEIT("f","df","fahrenheit"),
    KELVIN("k","kelvin", "kelvins");
}

fun main() {

    while (true) {
        println("Enter what you want to convert (or exit): ")

        val reader = Scanner(System.`in`)
        // input va dans un tableau
        val commande: List<String> = reader.nextLine().split(" ")
        if (commande[0].toLowerCase() == "exit") break
        // parcours du tableau pour récupérer les ordres et filtrer les mots vides
        val commandes = commande.filter { it.toLowerCase() != "degree" && it.toLowerCase() != "degrees" }
        val valSource: String = commandes[0]
        var unitSource: String = commandes[1]
        var unitDest: String = commandes[3]
        var missionImpossible = false

        missionImpossible = parseCommand(valSource,unitSource ,unitDest)

        if (!missionImpossible) {

            val convMasses: Map<String, Double> = mutableMapOf("g" to 1.00, "kg" to 1000.00, "mg" to 0.001, "lb" to 453.592, "oz" to 28.3495)
            val convLongueurs: Map<String, Double> = mutableMapOf("m" to 1.00, "km" to 1000.00, "cm" to 0.01, "mm" to 0.001,
                    "mi" to 1609.35, "yd" to 0.9144, "ft" to 0.3048, "in" to 0.0254)
            // résultat
            var resultat: Double = 0.0

            // maintenant on peut convertir
            val (unitSourceOK, unitSourceType, unitSourceKey) = checkUnit(unitSource)
            val (unitDestOK, unitDestType, unitDestKey) = checkUnit(unitDest)
            if (unitSourceType == "MASSES") {
              resultat = valSource.toDouble() * convMasses[unitSourceKey]!! / convMasses[unitDestKey]!!
            } else if (unitSourceType == "LONGUEURS") {
                resultat = valSource.toDouble() * convLongueurs[unitSourceKey]!! / convLongueurs[unitDestKey]!!
            } else if (unitSourceType == "TEMPERATURES") {
                resultat = computeDegrees(unitSourceKey,unitDestKey, valSource.toDouble())
            }

            printResultat(valSource.toDouble(), unitSource, resultat, unitSource,unitDest, unitSourceType, unitDestType )
        }

    }
}

data class idUnit (val unitOK : Boolean, val unitType :String, val unitKey : String)

fun checkUnit(unit: String ): idUnit {

    for (enum in MASSES.values()) {
        if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) return idUnit(true, "MASSES", enum.short)
    }
    for (enum in LONGUEURS.values()) {
        if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) return idUnit(true, "LONGUEURS", enum.short)
    }
    for (enum in TEMPERATURES.values()) {
        if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.medium || unit.toLowerCase() == enum.long) return idUnit(true, "TEMPERATURES", enum.short)
    }

    return idUnit(false, "NO VALID TYPE FOUND", "NO VALIDE TYPE FOUND")
}

fun computeDegrees(fromUnit : String, toUnit : String, value : Double) : Double{

    if (fromUnit=="c") {
        when (toUnit){
            "f" -> return value * 9 / 5 + 32
            "k" -> return value + 273.15
            "c" -> return value
        }
    }
    if (fromUnit=="f") {
        when (toUnit){
            "c" -> return (value-32) *5 / 9
            "k" -> return (value+459.67) * 5 / 9
            "f" -> return value
        }
    }
    if (fromUnit=="k") {
        when (toUnit){
            "f" -> return value * 9 / 5 - 459.67
            "c" -> return value - 273.15
            "k" -> return value
        }
    }

    return 0.0 // on espère ne jamais arriver ici :-)
}

fun printResultat (nbOrig : Double, unitOrig : String, resultat : Double, unitSrc : String, unitDest: String, unitSrcType : String, unitDestType : String) {
    var plurielOrig  = ""
    var plurielResult  = ""

    if (nbOrig != 1.00 ){
        when (unitSrcType) {
            "MASSES" -> {
                for (enum in MASSES.values()) {
                    if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.singular || unitSrc.toLowerCase() == enum.plural) plurielOrig = enum.plural
                }
            }
            "LONGUEURS" -> {
                for (enum in LONGUEURS.values()) {
                    if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.singular || unitSrc.toLowerCase() == enum.plural) plurielOrig = enum.plural
                }
            }
            "TEMPERATURES" -> {
                for (enum in TEMPERATURES.values()) {
                    if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.medium || unitSrc.toLowerCase() == enum.long) plurielOrig = maj(enum.long)
                }
            }
        }
    }
    else when (unitSrcType) {
        "MASSES" -> {
            for (enum in MASSES.values()) {
                if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.singular || unitSrc.toLowerCase() == enum.plural) plurielOrig = enum.singular
            }
        }
        "LONGUEURS" -> {
            for (enum in LONGUEURS.values()) {
                if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.singular || unitSrc.toLowerCase() == enum.plural) plurielOrig = enum.singular
            }
        }
        "TEMPERATURES" -> {
            for (enum in TEMPERATURES.values()) {
                if (unitSrc.toLowerCase() == enum.short || unitSrc.toLowerCase() == enum.medium || unitSrc.toLowerCase() == enum.long) plurielOrig = maj(enum.long)
                if (plurielOrig == "Kelvins") plurielOrig = "Kelvin"
            }
        }
    }

    if (resultat != 1.00 ){
        when (unitDestType) {
            "MASSES" -> {
                for (enum in MASSES.values()) {
                    if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.singular || unitDest.toLowerCase() == enum.plural) plurielResult = enum.plural
                }
            }
            "LONGUEURS" -> {
                for (enum in LONGUEURS.values()) {
                    if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.singular || unitDest.toLowerCase() == enum.plural) plurielResult = enum.plural
                }
            }
            "TEMPERATURES" -> {
                for (enum in TEMPERATURES.values()) {
                    if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.medium || unitDest.toLowerCase() == enum.long) plurielResult = maj(enum.long)
                }
            }
        }
    }
    else when (unitDestType) {
        "MASSES" -> {
            for (enum in MASSES.values()) {
                if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.singular || unitDest.toLowerCase() == enum.plural) plurielResult = enum.singular
            }
        }
        "LONGUEURS" -> {
            for (enum in LONGUEURS.values()) {
                if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.singular || unitDest.toLowerCase() == enum.plural) plurielResult = enum.singular
            }
        }
        "TEMPERATURES" -> {
            for (enum in TEMPERATURES.values()) {
                if (unitDest.toLowerCase() == enum.short || unitDest.toLowerCase() == enum.medium || unitDest.toLowerCase() == enum.long) plurielResult = maj(enum.medium)
            }
        }
    }

    // Gestion de "degree" et "degrees"
    if ((plurielOrig == "Celsius" || plurielOrig == "Fahrenheit" ) && nbOrig != 1.00) plurielOrig = "degrees " + plurielOrig
    if ((plurielOrig == "Celsius" || plurielOrig == "Fahrenheit" ) && nbOrig == 1.00) plurielOrig = "degree " + plurielOrig
    if ((plurielResult == "Celsius" || plurielResult == "Fahrenheit" ) && resultat != 1.00) plurielResult = "degrees " + plurielResult
    if ((plurielResult == "Celsius" || plurielResult == "Fahrenheit" ) && resultat == 1.00) plurielResult = "degree " + plurielResult
    
    println("$nbOrig $plurielOrig is $resultat $plurielResult")
}

fun maj (chaine: String) : String{
    var chainemaj = chaine.substring(0,1).toUpperCase()+chaine.substring(1,chaine.length)
    return chainemaj
}
class UnitéDeRéférence (singulier : String, pluriel : String)  {
    var singulier = singulier
    var pluriel = pluriel
}

// retourne une unité formattée proprement pour la sortie ; exemple centimeter pour cm, degree Celsius pour celsius
fun formatUnit (unit : String, type : String, sing : Boolean) : String{
    var formattedUnit : String = ""

    when (sing){
        // singulier
        true -> {
            if (type == "LONGUEURS") {
                for (enum in LONGUEURS.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) formattedUnit = enum.singular
                }
            }
            if (type == "MASSES") {
                for (enum in MASSES.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) formattedUnit = enum.singular
                }
            }
            if (type == "TEMPERATURES") {
                for (enum in TEMPERATURES.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.medium || unit.toLowerCase() == enum.long) formattedUnit = enum.medium
                }
                // gestion de la majuscule de début de mot pour ces 3 unités de températures
                formattedUnit = maj(formattedUnit)
                // ajout de "degree " devant l'unité
                if (formattedUnit.toLowerCase()!="kelvin") formattedUnit = "degree " + formattedUnit
            }
        }
        // pluriel
        false -> {
            if (type == "LONGUEURS") {
                for (enum in LONGUEURS.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) formattedUnit = enum.plural
                }
            }
            if (type == "MASSES") {
                for (enum in MASSES.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.singular || unit.toLowerCase() == enum.plural) formattedUnit = enum.plural
                }
            }
            if (type == "TEMPERATURES") {
                for (enum in TEMPERATURES.values()) {
                    if (unit.toLowerCase() == enum.short || unit.toLowerCase() == enum.medium || unit.toLowerCase() == enum.long) formattedUnit = enum.long
                }
                // gestion du "s" à la fin : à ajouter pour fahrenheit mais pas pour celsius ni kelvins
                if (!formattedUnit.endsWith('s')) formattedUnit + 's'
                // gestion de la majuscule de début de mot pour ces 3 unités de températures
                formattedUnit = maj(formattedUnit)
                // ajout de "degrees " devant l'unité sauf si Kelvins...
                if (formattedUnit.toLowerCase()!="kelvins") formattedUnit = "degrees " + formattedUnit
            }
        }
    }

    return formattedUnit
}

fun parseCommand (valSource : String,  unitSource : String, unitDest : String): Boolean {
    var unitSource = unitSource
    var unitDest = unitDest
    var missionImpossible = false
    val (unitSourceOK, unitSourceType, unitSourceKey) = checkUnit(unitSource)
    val (unitDestOK, unitDestType, unitDestKey) = checkUnit(unitDest)

    // il faut que valSource soit un Double
    var varSource =  valSource.toDoubleOrNull()
    if (varSource==null) {
        println("Parse error")
        return missionImpossible
    }

    // vérification si saisies négatives pour longueurs et masses
    if (varSource < 0.00 && unitSourceType != "TEMPERATURES") {
        if (unitSourceType == "LONGUEURS") println("Length shouldn't be negative")
        if (unitSourceType == "MASSES") println("Weight shouldn't be negative")
        missionImpossible = true
    }

    // il faut convertir la saisie en toutes letttres
    // gestion de l'unité source
    unitSource = formatUnit(unitSource, unitSourceType, false)
    // gestion de l'unité destination
    unitDest = formatUnit(unitDest, unitDestType, false)


    if (!unitSourceOK && !unitDestOK) {
        println("Conversion from ??? to ??? is impossible")
        missionImpossible = true
    } else if (!unitSourceOK && unitDestOK) {
        println("Conversion from ??? to $unitDest is impossible")
        missionImpossible = true
    } else if (unitSourceOK && !unitDestOK) {
        println("Conversion from $unitSource to ??? is impossible")
        missionImpossible = true
    } else if (!unitSourceType.equals(unitDestType)) {
        println("Conversion from $unitSource to $unitDest is impossible.")
        missionImpossible = true
    }

    return missionImpossible
}

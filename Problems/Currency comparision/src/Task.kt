import java.util.Scanner

enum class Pays (val libelle : String, val monnaie : String) {
    GERMANY ("Germany","Euro"),
    MALI ("Mali","CFA franc"),
    DOMINICA ("Dominica", "Eastern Caribbean dollar"),
    CANADA ("Canada", "Canadian dollar"),
    SPAIN ("Spain", "Euro"),
    AUSTRALIA ("Australia", "Australian dollar"),
    BRAZIL ("Brazil","Brazilian real"),
    SENEGAL ("Senegal", "CFA franc"),
    GRENADA ("Grenada","Eastern Caribbean dollar"),
    KIRIBATI("Kiribati", "Australian dollar"),
    FRANCE ("France", "Euro");

    companion object {
        fun getCurrency(country: String): String {

            for (enum in Pays.values()) {
                if (country == enum.libelle) return enum.monnaie
            }
            return ""
        }
    }
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val commande: List<String> = input.nextLine().split(" ")
    println(Pays.getCurrency(commande[0]).equals(Pays.getCurrency(commande[1])))

}
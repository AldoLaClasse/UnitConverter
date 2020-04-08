class Player(val id: Int, val name: String, val hp: Int){

    companion object {
        fun create (name : String) : Player{
            val name = name
            val hp = 100
            val id = 1
            return Player(id, name, hp)
        }
    }
}
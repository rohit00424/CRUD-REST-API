
import com.example.config.dbConfig
import com.example.model.*
import java.sql.ResultSet

class AccountController {
    fun accDetails(): MutableList<UserDataType> {
        var data = mutableListOf<UserDataType>()
        val selectQuery = dbConfig().conn().prepareStatement("SELECT id, username, password, fname, lname, email FROM public.\"tbl_login\"")
        val dataResult = selectQuery.executeQuery()
        while (dataResult.next()){
            val id = dataResult.getInt("id")
            val username = dataResult.getString("username")
            val password = dataResult.getString("password")
            val fname = dataResult.getString("fname")
            val lname = dataResult.getString("lname")
            val email = dataResult.getString("email")
            data.add(UserDataType(id,username,password,fname,lname,email))
        }
        return data
    }

    fun addUser(id:Int, uname:String, pass:String, fname:String, lname:String,email:String): Int {
        return dbConfig().conn().prepareStatement("INSERT INTO public.tbl_login (id, username, password, fname, lname, email) " +
                "VALUES ($id, '$uname', '$pass', '$fname', '$lname', '$email')").executeUpdate()
    }

    fun updateUserDetails(id:Int, uname:String, pass:String, fname:String, lname:String,email:String): Int {
        return dbConfig().conn().prepareStatement("UPDATE public.tbl_login SET username = '$uname', password= '$pass', fname= '$fname', lname= '$lname', email= '$email' WHERE id= $id").executeUpdate()
    }

    fun deleteUser(id:Int): Int {
        return dbConfig().conn().prepareStatement("DELETE FROM public.tbl_login WHERE id= $id").executeUpdate()
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex= Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(?:\\.[A-Za-z]{2,})?\$")
        return emailRegex.matches(email)
    }

    fun passwordCharacterCheck( password: String): Boolean {
        val passRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_+{}|:\"<>?`\\-=[\\\\]\\\\;',./]).{8,}\$")
        return passRegex.matches(password) && !password.contains(" ")
    }

    fun passwordLengthCheck( password: String): Boolean {
        return password.length >= 8
    }
}

//Gestão de Senhas (Cadastrar, Alterar, Excluir)

// Dependência para criptografia no build.gradle:
implementation 'com.google.crypto.tink:tink-android:1.6.1'

// Função para criptografar a senha
fun encryptPassword(password: String): String {
    val keysetHandle = KeysetHandle.generateNew(AeadConfig.KEY_TEMPLATE)
    val aead = keysetHandle.getPrimitive(Aead::class.java)
    val encryptedPassword = aead.encrypt(password.toByteArray(), null)
    return Base64.encodeToString(encryptedPassword, Base64.DEFAULT)
}

// Função para salvar a senha no Firestore
fun savePassword(uid: String, password: String, category: String) {
    val encryptedPassword = encryptPassword(password)
    
    val passwordData = hashMapOf(
        "password" to encryptedPassword,
        "category" to category,
        "accessToken" to generateAccessToken() // Gere um token aleatório
    )

    FirebaseFirestore.getInstance().collection("users")
        .document(uid)
        .collection("passwords")
        .add(passwordData)
        .addOnSuccessListener {
            Log.d("Senha", "Senha cadastrada com sucesso!")
        }
        .addOnFailureListener {
            Log.e("Senha", "Erro ao cadastrar senha: ${it.message}")
        }
}

// Função para gerar o accessToken
fun generateAccessToken(): String {
    val random = ByteArray(32) // 256 bits
    SecureRandom().nextBytes(random)
    return Base64.encodeToString(random, Base64.DEFAULT)
}

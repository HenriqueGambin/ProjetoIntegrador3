//Login Sem Senha (QR Code)

// Função para escanear o QR Code
fun scanQRCode() {
    val integrator = IntentIntegrator(this)
    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
    integrator.setBeepEnabled(true)
    integrator.setBarcodeImageEnabled(true)
    integrator.initiateScan()
}

// Após o scan, você deve pegar o loginToken do QRCode
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    if (result != null) {
        if (result.contents == null) {
            Log.e("QRCode", "Leitura cancelada")
        } else {
            val loginToken = result.contents // loginToken do QR Code
            verifyLoginToken(loginToken)
        }
    }
}

// Função para verificar o loginToken
fun verifyLoginToken(loginToken: String) {
    // Aqui você faria uma requisição para a Firebase Function para validar o loginToken
    val db = FirebaseFirestore.getInstance()
    db.collection("login").whereEqualTo("loginToken", loginToken)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                Log.e("Login", "Token inválido ou expirado")
            } else {
                val loginData = documents.first()
                val uid = loginData.getString("user") // UID do usuário
                // Agora você pode fazer o login com o UID
                performLogin(uid)
            }
        }
        .addOnFailureListener {
            Log.e("Login", "Erro ao verificar token: ${it.message}")
        }
}

// Função para realizar o login
fun performLogin(uid: String) {
    // Usar o Firebase Authentication para logar o usuário
    FirebaseAuth.getInstance().signInWithCustomToken(uid)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Login", "Login realizado com sucesso!")
            } else {
                Log.e("Login", "Erro ao realizar login: ${task.exception?.message}")
            }
        }
}

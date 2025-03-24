//Recuperação de Senha Mestre (Firebase Authentication)

// Função para enviar email de recuperação de senha
fun recoverMasterPassword(email: String) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Recuperação", "Email de recuperação enviado com sucesso.")
            } else {
                Log.e("Recuperação", "Erro ao enviar email de recuperação: ${task.exception?.message}")
            }
        }
}
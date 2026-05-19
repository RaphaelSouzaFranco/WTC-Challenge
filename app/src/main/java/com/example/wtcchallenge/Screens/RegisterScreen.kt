package com.example.wtcchallenge.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wtcchallenge.R
import com.example.wtcchallenge.network.RetrofitInstance
import com.example.wtcchallenge.network.dto.RegisterRequestDto
import com.example.wtcchallenge.ui.theme.WTCChallengeTheme
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onCadastroSucesso: () -> Unit, onVoltar: () -> Unit) {
    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var senha by remember { mutableStateOf(TextFieldValue("")) }
    var confirmarSenha by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFF293038),
        unfocusedContainerColor = Color(0xFF293038),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = Color.White,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121417)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logowtc),
                contentDescription = "Logo WTC",
                modifier = Modifier.size(180.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Criar conta",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            TextField(
                value = nome,
                onValueChange = { nome = it },
                placeholder = { Text("Nome completo", color = Color(0xFF9EABBA)) },
                singleLine = true,
                colors = fieldColors,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = Color(0xFF9EABBA)) },
                singleLine = true,
                colors = fieldColors,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = senha,
                onValueChange = { senha = it },
                placeholder = { Text("Senha (mín. 6 caracteres)", color = Color(0xFF9EABBA)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            TextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                placeholder = { Text("Confirmar senha", color = Color(0xFF9EABBA)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = fieldColors,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    errorMessage = null
                    when {
                        nome.text.isBlank() || email.text.isBlank() || senha.text.isBlank() ->
                            errorMessage = "Preencha todos os campos."
                        senha.text.length < 6 ->
                            errorMessage = "Senha deve ter ao menos 6 caracteres."
                        senha.text != confirmarSenha.text ->
                            errorMessage = "As senhas não coincidem."
                        else -> scope.launch {
                            isLoading = true
                            try {
                                RetrofitInstance.api.register(
                                    RegisterRequestDto(
                                        nome = nome.text.trim(),
                                        email = email.text.trim(),
                                        senha = senha.text
                                    )
                                )
                                onCadastroSucesso()
                            } catch (e: retrofit2.HttpException) {
                                errorMessage = when (e.code()) {
                                    409 -> "E-mail já cadastrado."
                                    else -> "Erro do servidor (${e.code()})."
                                }
                            } catch (e: java.net.ConnectException) {
                                errorMessage = "Não foi possível conectar ao servidor."
                            } catch (e: Exception) {
                                errorMessage = "Erro: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                } else {
                    Text(
                        text = "Cadastrar",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onVoltar) {
                Text("Já tenho conta. Entrar", color = Color(0xFF9EABBA), fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    WTCChallengeTheme {
        RegisterScreen(onCadastroSucesso = {}, onVoltar = {})
    }
}

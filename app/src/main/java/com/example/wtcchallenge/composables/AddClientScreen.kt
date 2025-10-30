package com.example.wtcchallenge.composables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    navController: NavController,
    // Função lambda para adicionar o cliente à lista (recebe o Cliente e volta)
    onSaveClient: (Cliente) -> Unit
) {
    // ESTADO DO FORMULÁRIO
    var formState by remember { mutableStateOf(ClienteFormState()) }
    var statusExpanded by remember { mutableStateOf(false) } // Para o Dropdown de Status

    // Opções de Status
    val statusOptions = listOf("Ativo", "Novo", "Inativo")

    Scaffold(
        containerColor = Color(0xFF121417),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Novo Cliente", color = Color.White, fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121417),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Formulário em LazyColumn para que o teclado não cubra os campos
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. NOME
                item {
                    CustomTextField(
                        value = TextFieldValue(formState.nome),
                        onValueChange = { formState = formState.copy(nome = it.text) },
                        placeholder = "Nome do Cliente/Empresa"
                    )
                }
                // 2. NÚMERO
                item {
                    CustomTextField(
                        value = TextFieldValue(formState.numero),
                        onValueChange = { formState = formState.copy(numero = it.text) },
                        placeholder = "Número (Telefone/WhatsApp)",
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
                    )
                }
                // 3. RAMO
                item {
                    CustomTextField(
                        value = TextFieldValue(formState.ramo),
                        onValueChange = { formState = formState.copy(ramo = it.text) },
                        placeholder = "Ramo de Atuação"
                    )
                }

                // 4. STATUS (Dropdown)
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF293038),
                            modifier = Modifier.fillMaxWidth().clickable { statusExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Status: ${formState.status}", color = Color.White)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White)
                            }
                        }
                        DropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false },
                            modifier = Modifier.background(Color(0xFF293038))
                        ) {
                            statusOptions.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status, color = Color.White) },
                                    onClick = {
                                        formState = formState.copy(status = status)
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // 5. SCORE
                item {
                    CustomTextField(
                        value = TextFieldValue(formState.score),
                        onValueChange = { formState = formState.copy(score = it.text) },
                        placeholder = "Score (0-100)",
                        keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                    )
                }

                // 6. TAGS
                item {
                    CustomTextField(
                        value = TextFieldValue(formState.tags),
                        onValueChange = { formState = formState.copy(tags = it.text) },
                        placeholder = "Tags (separar por vírgula, ex: VIP, Contrato)"
                    )
                }
            }

            // BOTÕES (Salvar e Cancelar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão Cancelar
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Botão Salvar
                Button(
                    onClick = {
                        // Converte o estado do formulário para o Cliente final
                        val novoCliente = Cliente(
                            nome = formState.nome,
                            numero = formState.numero,
                            ramo = formState.ramo,
                            status = formState.status,
                            score = formState.score.toIntOrNull() ?: 0, // Garante que é um número
                            tags = formState.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        )
                        onSaveClient(novoCliente)
                        navController.popBackStack() // Retorna à lista
                    },
                    // Desabilita se o Nome e o Ramo estiverem vazios
                    enabled = formState.nome.isNotEmpty() && formState.ramo.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
package com.example.wtcchallenge.composables

// Importações necessárias do Jetpack Compose e Material3
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults

// 🔸 Cores utilizadas no tema dos componentes
val darkSurface = Color(0xFF293038)      // Fundo escuro (background)
val lightText = Color.White              // Texto principal branco
val secondaryText = Color(0xFF9EABBA)    // Texto secundário (cinza azulado)

// ==========================================================
// 🔹 COMPONENTE: SimpleTextField
// ==========================================================
// Cria um campo de texto padrão com estilo personalizado (cor escura, bordas arredondadas, etc)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTextField(
    value: TextFieldValue,                   // Texto atual digitado
    onValueChange: (TextFieldValue) -> Unit, // Função chamada quando o valor muda
    label: String,                           // Texto de placeholder (dica do campo)
    modifier: Modifier = Modifier,           // Permite aplicar ajustes visuais externos
    minLines: Int = 1                        // Define se o campo será de uma linha ou múltiplas
) {
    TextField(
        value = value,                       // Valor do campo
        onValueChange = onValueChange,       // Callback ao digitar
        placeholder = { Text(label, color = secondaryText) }, // Texto de dica
        singleLine = minLines == 1,          // Se for uma linha, limita o campo
        minLines = minLines,                 // Define o mínimo de linhas
        colors = TextFieldDefaults.colors(   // Personaliza as cores do TextField
            focusedContainerColor = darkSurface,
            unfocusedContainerColor = darkSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = lightText,
            focusedTextColor = lightText,
            unfocusedTextColor = lightText,
            disabledTextColor = lightText
        ),
        shape = RoundedCornerShape(8.dp),    // Bordas arredondadas
        modifier = modifier.heightIn(        // Altura mínima do campo
            min = if (minLines > 1) 120.dp else 56.dp
        )
    )
}

// ==========================================================
// 🔹 COMPONENTE: ImageUploadBox
// ==========================================================
// Cria um box (cartão) para upload de imagem com botão e textos informativos
@Composable
fun ImageUploadBox(
    imageUri: android.net.Uri? = null,
    onPickImage: () -> Unit = {},
    onRemoveImage: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()                                              // Ocupar toda a largura
            .heightIn(min = 160.dp)                                      // Altura mínima
            .border(2.dp, secondaryText.copy(alpha = 0.5f), RoundedCornerShape(8.dp)) // Borda suave
            .background(darkSurface, RoundedCornerShape(8.dp))           // Fundo escuro com cantos arredondados
            .padding(16.dp),                                             // Espaçamento interno
        horizontalAlignment = Alignment.CenterHorizontally,              // Centraliza horizontalmente
        verticalArrangement = Arrangement.Center                         // Centraliza verticalmente
    ) {
        if (imageUri != null) {
            // Exibe preview da imagem selecionada
            androidx.compose.foundation.Image(
                painter = coil.compose.rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Imagem selecionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onPickImage,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Trocar Imagem", color = lightText)
                }
                OutlinedButton(
                    onClick = onRemoveImage,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Remover", color = Color(0xFFEF5350))
                }
            }
        } else {
            Text("Add Image", color = lightText, fontWeight = FontWeight.SemiBold) // Título
            Text("Supported formats: JPG, PNG", color = secondaryText, fontSize = 12.sp) // Subtexto
            Spacer(modifier = Modifier.height(12.dp))                         // Espaço entre os elementos
            Button(
                onClick = onPickImage,                                       // Clique do botão
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)), // Cor do botão
                shape = RoundedCornerShape(8.dp)                             // Borda arredondada
            ) {
                Text("Choose Image", color = lightText)                      // Texto do botão
            }
        }
    }
}

// ==========================================================
// 🔹 COMPONENTE: TargetAudienceDropdown
// ==========================================================
// Cria um menu suspenso (dropdown) para selecionar o público-alvo
@Composable
fun TargetAudienceDropdown(
    currentAudience: String,                 // Valor atual selecionado
    onAudienceSelected: (String) -> Unit,    // Callback quando o usuário escolhe uma opção
) {
    var isExpanded by remember { mutableStateOf(false) }
    val audienceOptions = listOf("Simple", "Segment", "Advanced", "Custom List")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(darkSurface)
            .clickable { isExpanded = true }                             // Abre o menu ao clicar
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(                                                            // Linha principal (texto + ícone)
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,            // Espaço entre os itens
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(currentAudience, color = lightText, fontSize = 16.sp)   // Mostra o valor atual
            Icon(
                Icons.Default.KeyboardArrowDown,                         // Ícone da seta para baixo
                null,
                tint = secondaryText,
                modifier = Modifier.size(24.dp)
            )
        }

        // Menu suspenso
        DropdownMenu(
            expanded = isExpanded,                                       // Controla abertura
            onDismissRequest = { isExpanded = false },                   // Fecha ao clicar fora
            modifier = Modifier.background(darkSurface)                  // Fundo escuro
        ) {
            audienceOptions.forEach { option ->                          // Cria uma linha para cada opção
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == currentAudience)
                                Color(0xFF1E88E5)                        // Cor de destaque se for o atual
                            else lightText                               // Caso contrário, cor normal
                        )
                    },
                    onClick = {
                        onAudienceSelected(option)                       // Atualiza o valor
                        isExpanded = false                               // Fecha o menu
                    },
                )
            }
        }
    }
}

@Composable
fun SegmentDropdown(
    segments: List<com.example.wtcchallenge.model.Segment>,
    selectedId: String?,
    onSelected: (String?) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedSegment = segments.find { it.id == selectedId }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(darkSurface)
            .clickable { isExpanded = true }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                selectedSegment?.let { "${it.nome} (${it.clientCount})" } ?: "Selecione um segmento",
                color = if (selectedSegment != null) lightText else secondaryText,
                fontSize = 16.sp
            )
            Icon(Icons.Default.KeyboardArrowDown, null, tint = secondaryText, modifier = Modifier.size(24.dp))
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(darkSurface)
        ) {
            segments.forEach { segment ->
                DropdownMenuItem(
                    text = {
                        Text(
                            "${segment.nome} (${segment.clientCount} clientes)",
                            color = if (segment.id == selectedId) Color(0xFF1E88E5) else lightText
                        )
                    },
                    onClick = {
                        onSelected(segment.id)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

// ==========================================================
// 🔹 COMPONENTE: CampaignPreviewCard
// ==========================================================
// Exibe uma prévia de campanha com título, descrição e imagem ilustrativa
@Composable
fun CampaignPreviewCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(darkSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {                         // Parte esquerda: textos
            Text("Express Campaign", color = secondaryText, fontSize = 12.sp) // Subtítulo
            Text(
                "Summer Special Offer",                                  // Título principal
                color = lightText,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Enjoy exclusive discounts on our new summer collection. Valid for a limited time!", // Descrição
                color = secondaryText,
                fontSize = 12.sp,
                maxLines = 3                                              // Limita o texto a 3 linhas
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(                                                             // Parte direita: imagem ilustrativa
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Gray)                                  // Caixa cinza representando a imagem
        )
    }
}

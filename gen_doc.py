# -*- coding: utf-8 -*-
"""Gera documentacao tecnica do backend WTC Challenge."""
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm, mm
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle,
    KeepTogether
)
from reportlab.lib.enums import TA_LEFT, TA_CENTER, TA_JUSTIFY
from reportlab.graphics.shapes import Drawing, Rect, String, Line, Polygon
from reportlab.graphics import renderPDF

OUTPUT = "WTC-Challenge-Backend-Doc.pdf"

styles = getSampleStyleSheet()

title_style = ParagraphStyle(
    "TitleX", parent=styles["Title"],
    fontSize=22, textColor=colors.HexColor("#1E88E5"),
    spaceAfter=12, alignment=TA_CENTER
)
h1 = ParagraphStyle(
    "H1", parent=styles["Heading1"],
    fontSize=16, textColor=colors.HexColor("#0D47A1"),
    spaceBefore=14, spaceAfter=8,
)
h2 = ParagraphStyle(
    "H2", parent=styles["Heading2"],
    fontSize=13, textColor=colors.HexColor("#1565C0"),
    spaceBefore=10, spaceAfter=6,
)
h3 = ParagraphStyle(
    "H3", parent=styles["Heading3"],
    fontSize=11, textColor=colors.HexColor("#37474F"),
    spaceBefore=6, spaceAfter=4,
)
body = ParagraphStyle(
    "Body", parent=styles["BodyText"],
    fontSize=10, leading=13, alignment=TA_JUSTIFY,
)
code = ParagraphStyle(
    "Code", parent=styles["BodyText"],
    fontName="Courier", fontSize=8, leading=10,
    leftIndent=10, textColor=colors.HexColor("#263238"),
    backColor=colors.HexColor("#ECEFF1"),
    borderPadding=4,
)
small = ParagraphStyle(
    "Small", parent=styles["BodyText"],
    fontSize=9, leading=11, alignment=TA_LEFT,
)


def architecture_diagram():
    """Diagrama de camadas do backend."""
    d = Drawing(480, 480)

    # Background
    d.add(Rect(0, 0, 480, 480, fillColor=colors.HexColor("#FAFAFA"),
               strokeColor=colors.HexColor("#CFD8DC")))

    # Title
    d.add(String(240, 460, "Arquitetura em Camadas - Spring Boot 3.2 + MongoDB",
                 fontSize=11, fillColor=colors.HexColor("#0D47A1"),
                 textAnchor="middle", fontName="Helvetica-Bold"))

    layers = [
        ("Cliente Android (Jetpack Compose)", "#1E88E5", 410),
        ("HTTPS / REST + JWT", "#90CAF9", 370),
        ("Controllers (REST API)", "#42A5F5", 320),
        ("Services (Regras de Negocio)", "#66BB6A", 260),
        ("Repositories (Spring Data)", "#FFA726", 200),
        ("MongoDB (NoSQL Document DB)", "#8E63CE", 140),
    ]
    for label, color, y in layers:
        is_arrow = (label == "HTTPS / REST + JWT")
        if is_arrow:
            d.add(Rect(80, y, 320, 25, fillColor=colors.HexColor(color),
                       strokeColor=colors.HexColor("#37474F")))
            d.add(String(240, y + 8, label, fontSize=9,
                         fillColor=colors.black, textAnchor="middle",
                         fontName="Helvetica-Bold"))
        else:
            d.add(Rect(60, y, 360, 40, fillColor=colors.HexColor(color),
                       strokeColor=colors.HexColor("#263238"),
                       strokeWidth=1.5))
            d.add(String(240, y + 16, label, fontSize=11,
                         fillColor=colors.white, textAnchor="middle",
                         fontName="Helvetica-Bold"))

    # Arrows between layers (down)
    arrow_xs = [240]
    arrow_pairs = [(450, 435), (395, 370), (360, 345), (300, 285),
                   (240, 225), (180, 165)]
    for top, bot in arrow_pairs:
        d.add(Line(240, top, 240, bot, strokeColor=colors.HexColor("#37474F"),
                   strokeWidth=1.5))
        # Arrowhead
        d.add(Polygon([235, bot + 4, 245, bot + 4, 240, bot - 2],
                      fillColor=colors.HexColor("#37474F"),
                      strokeColor=colors.HexColor("#37474F")))

    # Side annotations - cross-cutting concerns
    d.add(Rect(10, 200, 45, 220, fillColor=colors.HexColor("#FFCDD2"),
               strokeColor=colors.HexColor("#C62828")))
    d.add(String(32, 410, "Spring", fontSize=8, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))
    d.add(String(32, 400, "Security", fontSize=8, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))
    d.add(String(32, 388, "(JWT)", fontSize=7, fillColor=colors.black,
                 textAnchor="middle"))
    d.add(String(32, 300, "DTOs", fontSize=8, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))
    d.add(String(32, 220, "Auditing", fontSize=7, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))

    d.add(Rect(425, 200, 45, 220, fillColor=colors.HexColor("#C8E6C9"),
               strokeColor=colors.HexColor("#2E7D32")))
    d.add(String(447, 410, "SSE", fontSize=8, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))
    d.add(String(447, 398, "Real-time", fontSize=7, fillColor=colors.black,
                 textAnchor="middle"))
    d.add(String(447, 300, "Storage", fontSize=8, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))
    d.add(String(447, 290, "Service", fontSize=7, fillColor=colors.black,
                 textAnchor="middle"))
    d.add(String(447, 220, "Scheduler", fontSize=7, fillColor=colors.black,
                 textAnchor="middle", fontName="Helvetica-Bold"))

    # Footer
    d.add(String(240, 110, "Stack: Java 21 / Spring Boot 3.2 / Spring Data MongoDB / jjwt 0.12.5",
                 fontSize=8, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))
    d.add(String(240, 95, "Padroes: Builder, DTO, Repository, Dependency Injection",
                 fontSize=8, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))
    d.add(String(240, 80, "MongoDB rodando em: mongodb://localhost:27017/wtcdb",
                 fontSize=8, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))

    return d


def collections_diagram():
    """Diagrama das colecoes MongoDB."""
    d = Drawing(480, 360)
    d.add(Rect(0, 0, 480, 360, fillColor=colors.HexColor("#FAFAFA"),
               strokeColor=colors.HexColor("#CFD8DC")))
    d.add(String(240, 340, "Colecoes MongoDB e Relacoes",
                 fontSize=11, fillColor=colors.HexColor("#0D47A1"),
                 textAnchor="middle", fontName="Helvetica-Bold"))

    # Boxes for collections
    boxes = [
        ("operators",     60,  250, "#1E88E5"),
        ("clients",       190, 250, "#66BB6A"),
        ("conversations", 320, 250, "#FFA726"),
        ("messages",      320, 150, "#FF7043"),
        ("campaigns",     60,  150, "#8E63CE"),
        ("segments",      190, 150, "#26A69A"),
    ]
    for name, x, y, color in boxes:
        d.add(Rect(x, y, 110, 50, fillColor=colors.HexColor(color),
                   strokeColor=colors.HexColor("#263238"), strokeWidth=1.2))
        d.add(String(x + 55, y + 28, name, fontSize=11, fillColor=colors.white,
                     textAnchor="middle", fontName="Helvetica-Bold"))
        d.add(String(x + 55, y + 12, "_id, ...", fontSize=8,
                     fillColor=colors.white, textAnchor="middle"))

    # Relacoes (linhas)
    rels = [
        # operator -> clients
        (115, 250, 245, 275, "1:N"),
        # client -> conversations
        (245, 250, 375, 275, "1:N"),
        # conversation -> messages
        (375, 250, 375, 200, "1:N"),
        # operator -> campaigns
        (115, 250, 115, 200, "1:N"),
        # operator -> segments
        (115, 245, 245, 200, "1:N"),
        # segment -> clients
        (245, 200, 245, 250, "ref"),
    ]
    seen = set()
    for x1, y1, x2, y2, label in rels:
        key = (x1, y1, x2, y2)
        if key in seen:
            continue
        seen.add(key)
        d.add(Line(x1, y1, x2, y2, strokeColor=colors.HexColor("#455A64"),
                   strokeWidth=1, strokeDashArray=[2, 2]))
        d.add(String((x1 + x2) / 2 + 4, (y1 + y2) / 2 + 4, label,
                     fontSize=7, fillColor=colors.HexColor("#37474F")))

    # Legend
    d.add(String(240, 90, "Relacoes por referencia (operatorId, clientId, conversationId)",
                 fontSize=9, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))
    d.add(String(240, 75, "Indices: operatorId, clientId, conversationId, scheduledAt",
                 fontSize=9, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))
    d.add(String(240, 60, "Auditing automatico: createdAt (@CreatedDate), updatedAt (@LastModifiedDate)",
                 fontSize=9, fillColor=colors.HexColor("#37474F"),
                 textAnchor="middle"))
    return d


def endpoint_table(rows):
    """Table for endpoints. rows: list of (method, path, payload, response)."""
    header = ["Metodo", "Rota", "Payload", "Resposta"]
    data = [header] + rows
    # Wrap text in paragraphs for cells
    wrapped = [header]
    for r in rows:
        wrapped.append([
            Paragraph(f"<b>{r[0]}</b>", small),
            Paragraph(f"<font face='Courier' size='8'>{r[1]}</font>", small),
            Paragraph(r[2], small),
            Paragraph(r[3], small),
        ])
    t = Table(wrapped, colWidths=[1.7 * cm, 5.0 * cm, 5.3 * cm, 5.0 * cm])
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#1E88E5")),
        ("TEXTCOLOR",  (0, 0), (-1, 0), colors.white),
        ("FONTNAME",   (0, 0), (-1, 0), "Helvetica-Bold"),
        ("FONTSIZE",   (0, 0), (-1, 0), 9),
        ("ALIGN",      (0, 0), (-1, 0), "CENTER"),
        ("VALIGN",     (0, 0), (-1, -1), "TOP"),
        ("GRID",       (0, 0), (-1, -1), 0.4, colors.HexColor("#90A4AE")),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1),
         [colors.white, colors.HexColor("#ECEFF1")]),
        ("LEFTPADDING", (0, 0), (-1, -1), 4),
        ("RIGHTPADDING",(0, 0), (-1, -1), 4),
        ("TOPPADDING",  (0, 0), (-1, -1), 4),
        ("BOTTOMPADDING",(0, 0), (-1, -1), 4),
    ]))
    return t


def schema_table(fields):
    """Tabela do schema. fields: list of (campo, tipo, descricao)."""
    header = ["Campo", "Tipo", "Descricao"]
    data = [header]
    for f in fields:
        data.append([
            Paragraph(f"<font face='Courier' size='8'>{f[0]}</font>", small),
            Paragraph(f[1], small),
            Paragraph(f[2], small),
        ])
    t = Table(data, colWidths=[3.5 * cm, 3.0 * cm, 10.5 * cm])
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#37474F")),
        ("TEXTCOLOR",  (0, 0), (-1, 0), colors.white),
        ("FONTNAME",   (0, 0), (-1, 0), "Helvetica-Bold"),
        ("FONTSIZE",   (0, 0), (-1, 0), 9),
        ("ALIGN",      (0, 0), (-1, 0), "CENTER"),
        ("VALIGN",     (0, 0), (-1, -1), "TOP"),
        ("GRID",       (0, 0), (-1, -1), 0.3, colors.HexColor("#B0BEC5")),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1),
         [colors.white, colors.HexColor("#F5F5F5")]),
        ("LEFTPADDING", (0, 0), (-1, -1), 4),
        ("RIGHTPADDING",(0, 0), (-1, -1), 4),
        ("TOPPADDING",  (0, 0), (-1, -1), 3),
        ("BOTTOMPADDING",(0, 0), (-1, -1), 3),
    ]))
    return t


# =========================================================================
# Build PDF
# =========================================================================
doc = SimpleDocTemplate(OUTPUT, pagesize=A4,
                        leftMargin=1.8 * cm, rightMargin=1.8 * cm,
                        topMargin=1.6 * cm, bottomMargin=1.6 * cm)

story = []

# ====== CAPA ======
story.append(Spacer(1, 4 * cm))
story.append(Paragraph("WTC Challenge", title_style))
story.append(Paragraph("Documentacao Tecnica do Backend", title_style))
story.append(Spacer(1, 1.2 * cm))
story.append(Paragraph(
    "<para align='center'><b>FIAP - 2&deg; Ano - Fase 07</b><br/>"
    "Sprint 2<br/><br/>"
    "Stack: Java 21 / Spring Boot 3.2 / MongoDB / JWT</para>",
    body))
story.append(Spacer(1, 2 * cm))
story.append(Paragraph(
    "<para align='center'>Conteudo:<br/>"
    "1. Diagrama de Arquitetura do Backend<br/>"
    "2. Especificacao dos Endpoints<br/>"
    "3. Modelo de Dados NoSQL (MongoDB)</para>",
    body))
story.append(PageBreak())

# ====== 1. ARQUITETURA ======
story.append(Paragraph("1. Diagrama de Arquitetura do Backend", h1))

story.append(Paragraph(
    "O backend segue arquitetura em camadas (Layered Architecture) padrao Spring Boot, "
    "com separacao clara de responsabilidades. Cada requisicao HTTP percorre uma cadeia "
    "Controller &rarr; Service &rarr; Repository &rarr; MongoDB, atravessando filtros "
    "transversais de Seguranca (JWT) e validacao (Bean Validation).", body))
story.append(Spacer(1, 6))

story.append(architecture_diagram())
story.append(Spacer(1, 8))

story.append(Paragraph("Componentes principais", h2))
arch_items = [
    ("<b>Controllers</b> (REST API): expoem endpoints sob <font face='Courier'>/api/*</font>. "
     "Mapeiam requisicoes HTTP, validam entradas com <font face='Courier'>@Valid</font> "
     "e retornam <font face='Courier'>ResponseEntity&lt;DTO&gt;</font>."),
    ("<b>Services</b>: contem as regras de negocio (validacoes, orquestracao, transformacoes). "
     "Nao acessam o banco diretamente, delegam para os Repositories."),
    ("<b>Repositories</b> (Spring Data MongoDB): interfaces que estendem "
     "<font face='Courier'>MongoRepository&lt;T, String&gt;</font>, gerando queries "
     "automaticamente a partir dos nomes dos metodos."),
    ("<b>DTOs</b>: classes Request/Response separadas dos models, evitando exposicao "
     "direta das entidades persistentes. Padrao Builder utilizado consistentemente."),
    ("<b>Spring Security + JWT</b>: filtro <font face='Courier'>JwtAuthenticationFilter</font> "
     "intercepta requisicoes, valida o token Bearer e popula o "
     "<font face='Courier'>SecurityContext</font>. Rotas <font face='Courier'>/api/auth/**</font> "
     "sao publicas; as demais exigem autenticacao."),
    ("<b>SSE (Server-Sent Events)</b>: streaming de eventos em tempo real para a Inbox e "
     "conversas, permitindo entrega push sem polling."),
    ("<b>FileStorageService</b>: persiste midias de campanhas em "
     "<font face='Courier'>/uploads/campaigns/</font>, retornando o caminho relativo."),
    ("<b>Auditing</b>: <font face='Courier'>@EnableMongoAuditing</font> popula "
     "automaticamente <font face='Courier'>createdAt</font> e <font face='Courier'>updatedAt</font>."),
]
for item in arch_items:
    story.append(Paragraph("&bull; " + item, body))
    story.append(Spacer(1, 3))

story.append(Spacer(1, 6))
story.append(Paragraph("Fluxo de autenticacao", h2))
story.append(Paragraph(
    "1. Cliente envia <font face='Courier'>POST /api/auth/login</font> com email e senha.<br/>"
    "2. <font face='Courier'>AuthService</font> valida as credenciais via "
    "<font face='Courier'>BCryptPasswordEncoder</font> e gera dois tokens JWT "
    "(access: 1h; refresh: 7d).<br/>"
    "3. Cliente armazena ambos no <font face='Courier'>SessionManager</font> e injeta o "
    "access-token em <font face='Courier'>Authorization: Bearer ...</font>.<br/>"
    "4. Ao receber 401, o interceptor OkHttp chama <font face='Courier'>/api/auth/refresh</font> "
    "transparentemente e refaz a requisicao original.", body))

story.append(PageBreak())

# ====== 2. ENDPOINTS ======
story.append(Paragraph("2. Especificacao dos Endpoints", h1))
story.append(Paragraph(
    "Todos os endpoints sao expostos sob o prefixo <font face='Courier'>/api</font>. "
    "Salvo indicacao em contrario, requerem cabecalho "
    "<font face='Courier'>Authorization: Bearer &lt;token&gt;</font> "
    "e respondem <font face='Courier'>application/json</font>.", body))
story.append(Spacer(1, 6))

# --- AUTH ---
story.append(Paragraph("2.1 Autenticacao (<font face='Courier'>/api/auth</font>)", h2))
story.append(endpoint_table([
    ("POST", "/api/auth/login",
     "{ email, senha }",
     "200: { token, refreshToken, tokenType, operator }"),
    ("POST", "/api/auth/register",
     "{ nome, email, senha, cargo }",
     "201: { message, operatorId }"),
    ("POST", "/api/auth/refresh",
     "{ refreshToken }",
     "200: { token, refreshToken, tokenType, operator }"),
]))
story.append(Spacer(1, 8))

# --- OPERATORS ---
story.append(Paragraph("2.2 Operadores (<font face='Courier'>/api/operators</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/operators/{id}", "-", "200: Operator"),
    ("PUT", "/api/operators/{id}",
     "Operator (nome, cargo, notas, darkMode...)",
     "200: Operator atualizado"),
]))
story.append(Spacer(1, 8))

# --- CLIENTS ---
story.append(Paragraph("2.3 Clientes (<font face='Courier'>/api/clients</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/clients?search=&amp;status=&amp;minScore=&amp;maxScore=&amp;tag=",
     "Query params opcionais", "200: List&lt;Client&gt;"),
    ("GET", "/api/clients/{id}", "-", "200: Client"),
    ("POST", "/api/clients",
     "{ nome, numero, ramo, status, tags, score, operatorId }",
     "201: Client criado"),
    ("GET", "/api/clients/{id}/timeline", "-",
     "200: List&lt;TimelineEventDTO&gt; (CLIENT_CREATED, CONVERSATION_STARTED, MESSAGE_*)"),
]))
story.append(Spacer(1, 8))

# --- CONVERSATIONS ---
story.append(Paragraph("2.4 Conversas (<font face='Courier'>/api/conversations</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/conversations?operatorId=&amp;filter=Todos",
     "Query params", "200: List&lt;Conversation&gt;"),
    ("POST", "/api/conversations",
     "{ clientId, operatorId }",
     "200: Conversation (cria se nao existir)"),
]))
story.append(Spacer(1, 8))

# --- MESSAGES ---
story.append(Paragraph("2.5 Mensagens (<font face='Courier'>/api/messages</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/messages/{conversationId}?page=0&amp;size=30",
     "Paginacao",
     "200: Page&lt;Message&gt;"),
    ("POST", "/api/messages/{conversationId}",
     "{ senderId, senderType, content }",
     "201: Message"),
]))
story.append(Spacer(1, 8))

# --- INBOX ---
story.append(Paragraph("2.6 Inbox (<font face='Courier'>/api/inbox</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/inbox/{customerId}", "-",
     "200: List&lt;ConversationDTO&gt; (conversas do cliente)"),
    ("GET", "/api/inbox/{customerId}/messages?limit=50", "-",
     "200: List&lt;MessageDTO&gt;"),
]))
story.append(Spacer(1, 8))

# --- SEGMENTS ---
story.append(Paragraph("2.7 Segmentos (<font face='Courier'>/api/segments</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/segments?operatorId=...", "-", "200: List&lt;SegmentDTO&gt; (com clientCount)"),
    ("GET", "/api/segments/{id}", "-", "200: SegmentDTO"),
    ("POST", "/api/segments",
     "{ nome, descricao, operatorId, criterios{status,tag,minScore,maxScore,ramo}, clientIds }",
     "201: SegmentDTO"),
    ("PUT", "/api/segments/{id}",
     "Mesmo payload do POST",
     "200: SegmentDTO"),
    ("DELETE", "/api/segments/{id}", "-", "200: { message }"),
]))
story.append(Spacer(1, 8))

story.append(PageBreak())

# --- CAMPAIGNS ---
story.append(Paragraph("2.8 Campanhas (<font face='Courier'>/api/campaigns</font>)", h2))
story.append(endpoint_table([
    ("GET", "/api/campaigns?operatorId=&amp;status=",
     "Filtros opcionais",
     "200: List&lt;CampaignDTO&gt;"),
    ("GET", "/api/campaigns/{id}", "-", "200: CampaignDTO"),
    ("POST", "/api/campaigns",
     "{ titulo, mensagem, targetAudience, segmentId, operatorId }",
     "201: CampaignDTO (status=DRAFT)"),
    ("POST", "/api/campaigns/{id}/media",
     "multipart/form-data: file (JPG/PNG)",
     "200: { mediaUrl }"),
    ("POST", "/api/campaigns/{id}/send", "-",
     "200: CampaignDTO (status=SENT, sentAt preenchido)"),
    ("POST", "/api/campaigns/{id}/schedule",
     "{ scheduledAt: Instant ISO-8601 }",
     "200: CampaignDTO (status=SCHEDULED)"),
    ("POST", "/api/campaigns/{id}/abtest",
     "{ tituloB, mensagemB }",
     "200: List&lt;CampaignDTO&gt; (variantes A e B)"),
    ("DELETE", "/api/campaigns/{id}", "-",
     "200: { message } (remove a midia tambem)"),
]))
story.append(Spacer(1, 8))

# Codigos de status
story.append(Paragraph("2.9 Codigos de status comuns", h2))
status_data = [
    ["Codigo", "Significado", "Quando ocorre"],
    ["200 OK", "Sucesso", "Operacao concluida com retorno"],
    ["201 Created", "Recurso criado", "POST que cria entidade"],
    ["400 Bad Request", "Payload invalido", "Falha de @Valid / Bean Validation"],
    ["401 Unauthorized", "Token ausente/invalido", "Tratado via refresh automatico no cliente"],
    ["403 Forbidden", "Acesso negado", "Operador sem permissao para o recurso"],
    ["404 Not Found", "Recurso inexistente", "ID nao encontrado no MongoDB"],
    ["500 Internal Error", "Falha do servidor", "Excecao nao tratada (loggada)"],
]
t = Table(status_data, colWidths=[3.0 * cm, 4.0 * cm, 9.5 * cm])
t.setStyle(TableStyle([
    ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#1E88E5")),
    ("TEXTCOLOR",  (0, 0), (-1, 0), colors.white),
    ("FONTNAME",   (0, 0), (-1, 0), "Helvetica-Bold"),
    ("FONTSIZE",   (0, 0), (-1, -1), 9),
    ("ALIGN",      (0, 0), (0, -1), "CENTER"),
    ("VALIGN",     (0, 0), (-1, -1), "MIDDLE"),
    ("GRID",       (0, 0), (-1, -1), 0.3, colors.HexColor("#B0BEC5")),
    ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#ECEFF1")]),
    ("LEFTPADDING", (0, 0), (-1, -1), 5),
    ("TOPPADDING",  (0, 0), (-1, -1), 4),
    ("BOTTOMPADDING",(0, 0), (-1, -1), 4),
]))
story.append(t)

story.append(PageBreak())

# ====== 3. MODELO NoSQL ======
story.append(Paragraph("3. Modelo de Dados NoSQL (MongoDB)", h1))

story.append(Paragraph(
    "O banco utilizado e o <b>MongoDB</b> (NoSQL orientado a documentos). Cada entidade "
    "do dominio e mapeada para uma colecao via <font face='Courier'>@Document</font> "
    "(Spring Data MongoDB). Relacoes entre entidades sao representadas por "
    "<i>referencias</i> (campos como <font face='Courier'>operatorId</font>, "
    "<font face='Courier'>clientId</font>, <font face='Courier'>conversationId</font>) "
    "e nao por embedding profundo, permitindo escalabilidade e consultas independentes.", body))
story.append(Spacer(1, 6))

story.append(collections_diagram())
story.append(Spacer(1, 8))

story.append(Paragraph("Configuracao", h2))
story.append(Paragraph(
    "<font face='Courier'>spring.data.mongodb.uri = mongodb://localhost:27017/wtcdb</font><br/>"
    "Auditing habilitado via <font face='Courier'>@EnableMongoAuditing</font>.", body))

# --- operators ---
story.append(Paragraph("3.1 operators", h2))
story.append(Paragraph("Colecao de operadores (usuarios autenticados do sistema).", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico (gerado pelo Mongo)."),
    ("nome", "String", "Nome do operador."),
    ("email", "String (unique)", "Email para login. Indice unico."),
    ("senhaHash", "String", "Hash BCrypt da senha."),
    ("cargo", "String", "Cargo (ex.: Atendente, Supervisor)."),
    ("avatarUrl", "String?", "Caminho da imagem de perfil."),
    ("darkMode", "Boolean", "Preferencia de tema."),
    ("notas", "String?", "Notas livres do operador."),
    ("createdAt", "Instant", "Preenchido por @CreatedDate."),
]))
story.append(Spacer(1, 8))

# --- clients ---
story.append(Paragraph("3.2 clients", h2))
story.append(Paragraph("Clientes/leads gerenciados pelo operador.", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico."),
    ("nome", "String", "Nome do cliente."),
    ("numero", "String", "Telefone/WhatsApp."),
    ("ramo", "String", "Ramo de atuacao."),
    ("status", "String", "Lead | Prospect | Ativo | Inativo."),
    ("tags", "List&lt;String&gt;", "Tags livres (ex.: VIP, Recorrente)."),
    ("score", "Integer (0-100)", "Pontuacao do cliente."),
    ("operatorId", "String (indexed)", "Referencia ao operador dono. Indice."),
    ("createdAt", "Instant", "@CreatedDate."),
    ("updatedAt", "Instant", "@LastModifiedDate."),
]))
story.append(Spacer(1, 8))

story.append(PageBreak())

# --- conversations ---
story.append(Paragraph("3.3 conversations", h2))
story.append(Paragraph("Conversa entre um operador e um cliente. Uma por par.", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico."),
    ("clientId", "String (indexed)", "Referencia ao cliente."),
    ("operatorId", "String (indexed)", "Referencia ao operador."),
    ("lastMessage", "String?", "Conteudo da ultima mensagem (cache)."),
    ("lastMessageAt", "Instant?", "Timestamp ordenacao."),
    ("unreadCount", "Integer", "Quantidade de mensagens nao lidas."),
    ("status", "String", "Aberta | Fechada."),
    ("createdAt", "Instant", "@CreatedDate."),
]))
story.append(Spacer(1, 8))

# --- messages ---
story.append(Paragraph("3.4 messages", h2))
story.append(Paragraph("Mensagens individuais de uma conversa.", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico."),
    ("conversationId", "String (indexed)", "Referencia a conversation. Indice."),
    ("senderId", "String", "ID do remetente."),
    ("senderType", "String", "OPERATOR | CLIENT."),
    ("content", "String", "Conteudo textual."),
    ("read", "Boolean", "Se foi lida."),
    ("createdAt", "Instant (indexed)", "@CreatedDate. Indice para ordenacao."),
]))
story.append(Spacer(1, 8))

# --- segments ---
story.append(Paragraph("3.5 segments", h2))
story.append(Paragraph("Agrupamentos dinamicos de clientes para campanhas.", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico."),
    ("nome", "String", "Nome do segmento."),
    ("descricao", "String?", "Descricao livre."),
    ("operatorId", "String (indexed)", "Operador dono."),
    ("criterios", "Map&lt;String, Object&gt;",
     "Filtros dinamicos: status, tag, minScore, maxScore, ramo."),
    ("clientIds", "List&lt;String&gt;?",
     "Lista explicita de clientes (alternativa aos criterios)."),
    ("createdAt", "Instant", "@CreatedDate."),
    ("updatedAt", "Instant", "@LastModifiedDate."),
]))
story.append(Spacer(1, 8))

# --- campaigns ---
story.append(Paragraph("3.6 campaigns", h2))
story.append(Paragraph("Campanhas de marketing (envio massivo de mensagens).", body))
story.append(schema_table([
    ("_id", "ObjectId/String", "Identificador unico."),
    ("titulo", "String", "Titulo da campanha."),
    ("mensagem", "String", "Texto enviado."),
    ("targetAudience", "String", "Simple | Segment | Advanced | Custom List."),
    ("segmentId", "String?", "Referencia a segments quando aplicavel."),
    ("mediaUrl", "String?", "Caminho do arquivo enviado (multipart upload)."),
    ("status", "String", "DRAFT | SCHEDULED | SENT."),
    ("operatorId", "String (indexed)", "Operador criador."),
    ("scheduledAt", "Instant?", "Momento agendado para envio."),
    ("sentAt", "Instant?", "Momento real do envio."),
    ("variantOf", "String?", "ID da campanha original (teste A/B)."),
    ("variantLabel", "String?", "A | B (teste A/B)."),
    ("createdAt", "Instant", "@CreatedDate."),
]))
story.append(Spacer(1, 8))

story.append(Paragraph("Justificativa do NoSQL", h2))
story.append(Paragraph(
    "MongoDB foi escolhido por: (a) flexibilidade no schema dos <i>criterios</i> de segmentos "
    "(campo aninhado <font face='Courier'>Map&lt;String,Object&gt;</font> sem migrations); "
    "(b) escrita rapida de mensagens em alto volume; (c) facilidade de evolucao do modelo "
    "(adicao de campos como <font face='Courier'>variantOf</font> e "
    "<font face='Courier'>scheduledAt</font> sem migracao); "
    "(d) integracao nativa com Spring Data via "
    "<font face='Courier'>MongoRepository</font>, eliminando boilerplate.", body))

story.append(Spacer(1, 14))
story.append(Paragraph(
    "<para align='center'><i>WTC Challenge - FIAP - 2 Ano - Fase 07 - Sprint 2</i></para>",
    small))

doc.build(story)
print(f"OK -> {OUTPUT}")

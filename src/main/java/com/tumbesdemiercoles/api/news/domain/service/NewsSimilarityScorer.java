package com.tumbesdemiercoles.api.news.domain.service;

import com.tumbesdemiercoles.api.news.domain.model.News;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class NewsSimilarityScorer {

  private static final Set<String> STOP_WORDS = loadStopWords();
  private static final double TITLE_WEIGHT = 0.6;
  private static final double CONTENT_WEIGHT = 0.4;
  private static final double CATEGORY_BOOST = 1.5;
  private static final int MAX_CONTENT_TOKENS = 200;

  public double score(News source, News candidate) {
    Set<String> sourceTitleTokens = tokenize(source.getTitle());
    Set<String> sourceContentTokens = truncateTokens(tokenize(source.getContent()), MAX_CONTENT_TOKENS);
    Set<String> candidateTitleTokens = tokenize(candidate.getTitle());
    Set<String> candidateContentTokens = truncateTokens(tokenize(candidate.getContent()), MAX_CONTENT_TOKENS);

    double titleScore = jaccardSimilarity(sourceTitleTokens, candidateTitleTokens);
    double contentScore = jaccardSimilarity(sourceContentTokens, candidateContentTokens);

    double score = titleScore * TITLE_WEIGHT + contentScore * CONTENT_WEIGHT;

    if (source.getCategoryId() != null && source.getCategoryId().equals(candidate.getCategoryId())) {
      score *= CATEGORY_BOOST;
    }

    return score;
  }

  private double jaccardSimilarity(Set<String> a, Set<String> b) {
    if (a.isEmpty() && b.isEmpty()) {
      return 0.0;
    }
    Set<String> intersection = new HashSet<>(a);
    intersection.retainAll(b);
    Set<String> union = new HashSet<>(a);
    union.addAll(b);
    return (double) intersection.size() / union.size();
  }

  Set<String> tokenize(String text) {
    if (text == null || text.isBlank()) {
      return Set.of();
    }
    return Arrays.stream(text.toLowerCase().split("[^\\p{L}]+"))
        .filter(token -> token.length() >= 3)
        .filter(token -> !STOP_WORDS.contains(token))
        .collect(Collectors.toSet());
  }

  private Set<String> truncateTokens(Set<String> tokens, int max) {
    if (tokens.size() <= max) {
      return tokens;
    }
    return tokens.stream().limit(max).collect(Collectors.toSet());
  }

  private static Set<String> loadStopWords() {
    return Set.of(
        "abdomen", "abril", "absoluto", "abstracto", "abuso", "acabar", "acceso", "acción", "aceptar",
        "acerca", "acuerdo", "además", "adquirir", "afectar", "afuera", "agosto", "agregar", "agua",
        "ahora", "alcanzar", "algo", "algún", "alguna", "algunas", "algunos", "alrededor", "alta",
        "alto", "ambas", "ambos", "amplio", "andar", "anexo", "anterior", "antes", "anual", "anzuelo",
        "año", "años", "aparecer", "apenas", "aplicar", "apoyar", "aprender", "aquella", "aquellas",
        "aquello", "aquellos", "aquel", "aquí", "archivo", "archivos", "arrancar", "arriba", "asegurar",
        "así", "atrás", "aumentar", "aún", "autor", "avance", "ayuda", "a", "abajo", "ante",
        "atras", "ayer", "baja", "bajo", "barrer", "base", "bastante", "bien", "blanco", "bolsa",
        "bonito", "breve", "buen", "buena", "buenas", "bueno", "buenos", "buscar", "cabal", "cada",
        "cadena", "caer", "calcular", "cambiar", "cambio", "campaña", "campo", "cantidad", "capaz",
        "capital", "cara", "carga", "carne", "carrera", "casi", "caso", "casos", "causa", "cerca",
        "cesar", "cierto", "cinco", "cinta", "círculo", "ciudad", "claro", "clase", "cliente", "cobrar",
        "coche", "codigo", "coger", "colocar", "comenzar", "comer", "como", "cómo", "compañía", "completo",
        "comprar", "comprender", "común", "comunes", "con", "conceder", "concepto", "conciencia", "concurso",
        "condición", "conectar", "conjunto", "conocer", "conseguir", "consejo", "consigo", "consistir",
        "constante", "constituir", "construir", "consulta", "contacto", "contar", "contener", "contenido",
        "contra", "control", "conversar", "convertir", "coordinar", "correr", "corresponder", "cosa",
        "cosas", "costa", "costar", "crear", "crecer", "creer", "cruce", "cuadro", "cual", "cualquier",
        "cualquiera", "cuál", "cuando", "cuándo", "cuanto", "cuánto", "cuarto", "cuatro", "cubrir",
        "cuenta", "cuento", "cuerpo", "cuestión", "cumplir", "cuyo", "da", "dicho", "dada", "dan",
        "dar", "de", "debe", "deber", "debería", "decir", "decisión", "declarar", "dejar", "del",
        "delante", "demás", "demasiado", "depender", "derecha", "derecho", "desarrollar", "desarrollo",
        "desde", "desear", "despacio", "después", "destacar", "destino", "día", "días", "diario", "diez",
        "diferencia", "diferente", "diferentes", "difícil", "dinero", "dirección", "directo", "director",
        "dirigir", "disco", "disponer", "distinto", "distintos", "diverso", "diversos", "doctor", "doler",
        "donde", "dónde", "dormir", "dos", "duda", "durante", "duro", "e", "echar", "económico", "edad",
        "edificio", "editor", "efecto", "ejemplo", "ejecutar", "ejercer", "ejército", "el", "él", "ella",
        "ellas", "ello", "ellos", "embargo", "emitir", "empezar", "empresa", "en", "encargar", "encerrar",
        "encima", "encontrar", "encuentra", "enero", "enfocar", "enfrente", "engañar", "enviar", "equipo",
        "equivocar", "error", "esa", "esas", "escena", "escoger", "escribir", "escrito", "escuela", "ese",
        "eso", "esos", "espacio", "especial", "especie", "esperar", "espíritu", "esta", "ésta", "está",
        "establecer", "estación", "estado", "estados", "estar", "estas", "éste", "este", "esto", "estos",
        "éxito", "experiencia", "experto", "explicar", "expresar", "extender", "extensión", "extraño",
        "fácil", "familia", "familiar", "favor", "febrero", "feliz", "fenómeno", "feria", "fiar", "fiel",
        "fiesta", "figura", "fijar", "fijo", "fin", "final", "finca", "firmar", "física", "físico",
        "flor", "fondo", "forma", "formar", "foto", "fracasar", "franco", "frente", "fresco", "frío",
        "fruta", "fuego", "fuera", "fuerte", "fuerza", "funcionar", "función", "futuro", "gabinete",
        "ganar", "gas", "gasto", "general", "género", "gente", "gestión", "girar", "gobierno", "golpe",
        "gracia", "gracias", "grado", "gran", "grande", "grandes", "grave", "grupo", "guerra", "gustar",
        "gusto", "haber", "había", "hablar", "hacer", "hacia", "hasta", "hecho", "helado", "hermano",
        "herramienta", "hijo", "historia", "hizo", "hogar", "hoja", "hombre", "hombres", "hora", "hoy",
        "hubo", "huir", "humano", "idea", "identificar", "igual", "imagen", "imaginación", "impedir",
        "implicar", "imponer", "importancia", "importante", "importar", "imposible", "impresión",
        "incluir", "incluyendo", "incluso", "individual", "industrial", "información", "informar",
        "informe", "inicio", "inmediato", "institución", "instrumento", "interés", "interesar",
        "interior", "internacional", "interno", "intentar", "intervenir", "introducir",
        "inversión", "investigar", "ir", "izquierda", "jamás", "jefe", "joven", "jóvenes", "jugar",
        "jugo", "julio", "junio", "junto", "jurar", "justicia", "justo", "labor", "lado", "largo",
        "las", "lástima", "le", "lección", "lectura", "leer", "legal", "lejos", "lengua", "lento",
        "los", "lo", "la", "llegar", "llenar", "llevar", "llamar", "llano", "llegada", "lleno",
        "llorar", "llover", "local", "lograr", "lucha", "luego", "lugar", "luz", "madera",
        "madre", "maestro", "magia", "mal", "malo", "mandar", "manera", "manifestar", "mantener",
        "mano", "máquina", "mar", "marcar", "marco", "marcha", "maría", "marino", "más", "masa",
        "matar", "material", "materia", "mayor", "mayoría", "mecanismo", "media", "mediano", "mediante",
        "medida", "medio", "medios", "mejor", "mejora", "memoria", "menor", "menos", "mensaje",
        "mente", "mercado", "mes", "meta", "meter", "metro", "mi", "miedo", "mientras",
        "mil", "militar", "millón", "minuto", "mío", "mira", "mirar", "misma", "mismas", "mismo",
        "mismos", "mitad", "modo", "momento", "moneda", "monto", "moral", "morir", "mover",
        "movimiento", "mucha", "muchas", "mucho", "muchos", "muerte", "muestra", "mujer", "mujeres",
        "mundo", "municipal", "música", "muy", "nacer", "nación", "nacional", "nada", "nadie",
        "natural", "naturaleza", "necesario", "necesidad", "necesitar", "negar", "negocio", "negra",
        "negro", "nervio", "ni", "ningún", "ninguna", "ningunas", "ningunos", "niño", "niños",
        "nivel", "no", "noche", "nombre", "nor", "norte", "nos", "nosotras", "nosotros", "nota",
        "notar", "noticia", "novedad", "noviembre", "nuestra", "nuestras", "nuestro", "nuestros",
        "nuevo", "nuevos", "nunca", "o", "objeto", "objetivo", "obligar", "obra", "observar",
        "obtener", "ocasión", "octubre", "ocupar", "ocurrir", "ocho", "odio", "oficial", "ofrecer",
        "oído", "oír", "ojear", "ojos", "ola", "oler", "olvidar", "once", "operación", "operar",
        "opinar", "oportunidad", "oposición", "optar", "oración", "orden", "ordenar",
        "oreja", "organismo", "organizar", "origen", "original", "oro", "os", "otra", "otras",
        "otro", "otros", "paciente", "pacto", "padre", "pagar", "página", "país", "países",
        "palabra", "pan", "papel", "par", "para", "parar", "parecer", "pared", "parte", "partes",
        "partir", "pasado", "pasar", "paso", "pausa", "particular", "partido", "poco", "pocos",
        "poder", "podría", "poner", "por", "porque", "posición", "posible", "posibilidad",
        "posterior", "precio", "preciso", "preferir", "pregunta", "prensa", "preparar",
        "presentar", "presente", "presidente", "presión", "prestar", "pretender", "primer", "primera",
        "primero", "primeros", "principal", "principio", "privado", "probable", "problema", "proceso",
        "producir", "producto", "profesor", "programa", "progreso", "prometer", "pronto", "propia",
        "propias", "propietario", "propio", "propios", "propósito", "proteger", "protesta", "proveer",
        "provincia", "próximo", "proyecto", "prueba", "público", "pueblo", "puerta", "pues", "puesto",
        "punto", "que", "qué", "quedar", "queja", "quemar", "querer", "quien", "quién", "quienes",
        "quiere", "quinientos", "quinto", "quitar", "quizá", "quizás", "raíz", "rango", "rapidez",
        "rápido", "raro", "rasgo", "rato", "razón", "reacción", "real", "realidad",
        "realizar", "recibir", "reciente", "reclamo", "recoger", "reconocer", "recordar",
        "recorrer", "recuperar", "reducir", "referir", "reflejar", "regalar", "régimen", "región",
        "registrar", "regla", "regresar", "reinar", "reír", "relación", "relacionar", "religión",
        "relleno", "rematar", "rendir", "renovar", "renta", "reparar", "repente", "repetir", "reporte",
        "representar", "resolver", "respetar", "responder", "responsable", "resto", "resultado",
        "resultar", "retención", "retirar", "retorno", "reunión", "reunir", "revelar", "revisar",
        "revista", "rey", "rezar", "rico", "riesgo", "río", "ritmo", "robar", "rodear", "rojo",
        "romper", "ropa", "rubro", "rueda", "rural", "s", "saber", "sabor", "sacar", "salida",
        "salir", "salón", "saltar", "salud", "salvar", "san", "sangre", "santo", "se", "secar",
        "sección", "seco", "secreto", "sector", "según", "segundo", "seguridad", "seis", "semana",
        "semejante", "sentar", "sentido", "sentir", "señal", "señalar", "señor", "separar",
        "septiembre", "ser", "serie", "servicio", "servir", "sesión", "setiembre", "si", "sido",
        "siempre", "siendo", "siente", "siete", "siglo", "significado", "significar", "siguiente",
        "sin", "sino", "sistema", "situación", "situar", "sobre", "social", "sociedad", "socio",
        "solicitar", "solo", "sólo", "solución", "sombra", "son", "sonar", "sonrisa", "sostener",
        "su", "suave", "subir", "suceder", "sueldo", "suelo", "sueño", "suerte", "suficiente",
        "sufrir", "sujeto", "suma", "sumar", "superar", "superior", "suponer", "surgir", "suyo",
        "tabla", "tal", "tamaño", "también", "tampoco", "tan", "tanto", "tarde", "tarea", "tarjeta",
        "te", "té", "teatro", "técnica", "técnico", "tejido", "tema", "temblar", "temer", "temor",
        "temperatura", "templo", "temporada", "tender", "tener", "tensión", "teoría", "tercer",
        "tercero", "terminar", "término", "terreno", "tesis", "testigo", "ti", "tibio", "tiempo",
        "tienda", "tierra", "tirar", "título", "tocar", "todavía", "todo", "todos", "tomar", "tono",
        "tonto", "torcer", "torre", "total", "trabajar", "trabajo", "tradición", "traer", "trafico",
        "tragar", "traje", "tranquilo", "transformar", "tratar", "tras", "travesía",
        "treinta", "tren", "tres", "tribu", "tribunal", "trigo", "triunfo", "tropezar", "tropa",
        "tropical", "tu", "tú", "tumba", "tumbar", "tumor", "túnica", "turbio", "turno", "tuyo",
        "último", "ultimar", "un", "una", "unas", "único", "unidad", "unificar", "unión", "unir",
        "uno", "unos", "urbano", "usar", "uso", "usted", "útil", "utilizar", "vaca", "vacío",
        "vagar", "vago", "valer", "válido", "valor", "varios", "vaso", "vecino", "vector", "vehículo",
        "veinte", "vela", "velocidad", "vencer", "vender", "venir", "venta", "ventana", "ver",
        "verano", "verbo", "verdad", "verdadero", "verde", "versión", "vez", "viajar", "viaje",
        "vida", "video", "viejo", "viento", "viernes", "vigilar", "vino", "violencia",
        "virar", "virtud", "visión", "visitar", "visita", "vista", "vital", "vivienda", "vivir",
        "vivo", "vocabulario", "volar", "volver", "vos", "vosotras", "vosotros", "voz", "vuelo",
        "vuelta", "vuelto", "y", "ya", "yacer", "yegua", "yema", "yendo", "yerba", "yeso", "yo",
        "zona", "zorro"
    );
  }
}

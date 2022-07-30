import { Topic } from 'src/app/helpers/models/topic';
export interface Evaluation {
   id?:string,
   date?:Date,
   topic?:Topic,
   workEvaluation?:any[],
   productEvaluation?:any[],
   commentary?:string ,
   finalNote:number
}
export var TitulationWorkEvaluation=[
    {
        evaluationCriteria:'1. Planteamiento del problema',
        questions:'¿Responde a las necesidades del contexto social? ¿Es relevante? ¿Se justifica adecuadamente?',
        ponderation:10,
        
    },
    {
        evaluationCriteria:'2. El problema',
        questions:'¿El problema está presentado de manera clara y precisa? ¿la justificación avala la realización del trabajo? ¿existe congruencia entre el objetivo general con el tema y la formulación del problema? ¿los objetivos específicos conducen al logro del objetivo general? los objetivos específicos están formulados de manera correcta?',
        ponderation:10,

    },
    {
        evaluationCriteria:'3. Marco teórico',
        questions:'¿Los antecedentes guardan relación con la investigación y están presentados correctamente? ¿las bases teóricas están enfocadas en el problema de investigación del cual se ocupa? ¿las bases teóricas presentan una estructura lógica y coherente?',
        ponderation:10,

    },
    {
        evaluationCriteria:'4. Marco metodológico ',
        questions:'¿El diseño, la muestra, los procedimientos de recogida de información y las técnicas de análisis de datos se exponen de forma detallada y adecuada? ¿Los instrumentos presentan índices razonables de fiabilidad y validez? ¿los procedimientos de recolección y análisis de datos son presentados de manera clara y detallada?',
        ponderation:10,

    },
    {
        evaluationCriteria:'5. Análisis, interpretación conclusiones y recomendaciones ',
        questions:'¿Los resultados son presentados de forma clara y coherente en función a los objetivos planteados? ¿son comprensibles? ¿se justifican a partir de los datos obtenidos? ¿las conclusiones responden a los objetivos? ¿existen discrepancias entre las conclusiones? ¿se efectúan propuestas y recomendaciones viables?',
        ponderation:10,

    },
    {
        evaluationCriteria:'6. Cumplimiento de normas (Libro cómo investigar en la UNIBE)',
        questions:'¿Se cumple con las normas para el tamaño de página, márgenes, tipo y tamaño de letra, interlineado, títulos y subtítulos? ¿las citas se presentan como se establece en la normativa de la universidad? ¿las fuentes de información están actualizadas? ¿se especifican en las bibliografías las fuentes utilizadas? ¿las bibliografías cumplen con las normas establecidas para cada tipo de fuente? ¿las imágenes, fotografías, gráficos y mapas se encuentran referenciado de forma correcto y cuentan con el tamaño establecido?',
        ponderation:5,

    },
    {
        evaluationCriteria:'7.Sintaxis ',
        questions:'¿Está bien escrito el trabajo de titulación? ¿se entienden las ideas del documento en cada una de las secciones? ¿se cumplen con las normas de acentuación y uso de mayúsculas? ¿el documento está redactado en el tiempo verbal adecuado?',
        ponderation:5,

    },
]
export var TitulationWorkEvaluationWithoutProduct=[
    {
        evaluationCriteria:'1. Planteamiento del problema',
        questions:'¿Responde a las necesidades del contexto social? ¿Es relevante? ¿Se justifica adecuadamente?',
        ponderation:10,

    },
    {
        evaluationCriteria:'2. El problema',
        questions:'¿El problema está presentado de manera clara y precisa? ¿la justificación avala la realización del trabajo? ¿existe congruencia entre el objetivo general con el tema y la formulación del problema? ¿los objetivos específicos conducen al logro del objetivo general? los objetivos específicos están formulados de manera correcta?',
        ponderation:15,

    },
    {
        evaluationCriteria:'3. Marco teórico',
        questions:'¿Los antecedentes guardan relación con la investigación y están presentados correctamente? ¿las bases teóricas están enfocadas en el problema de investigación del cual se ocupa? ¿las bases teóricas presentan una estructura lógica y coherente?',
        ponderation:15,

    },
    {
        evaluationCriteria:'4. Marco metodológico ',
        questions:'¿El diseño, la muestra, los procedimientos de recogida de información y las técnicas de análisis de datos se exponen de forma detallada y adecuada? ¿Los instrumentos presentan índices razonables de fiabilidad y validez? ¿los procedimientos de recolección y análisis de datos son presentados de manera clara y detallada?',
        ponderation:15,

    },
    {
        evaluationCriteria:'5. Análisis, interpretación conclusiones y recomendaciones ',
        questions:'¿Los resultados son presentados de forma clara y coherente en función a los objetivos planteados? ¿son comprensibles? ¿se justifican a partir de los datos obtenidos? ¿las conclusiones responden a los objetivos? ¿existen discrepancias entre las conclusiones? ¿se efectúan propuestas y recomendaciones viables?',
        ponderation:20,

    },
    {
        evaluationCriteria:'6. Cumplimiento de normas (Libro cómo investigar en la UNIBE)',
        questions:'¿Se cumple con las normas para el tamaño de página, márgenes, tipo y tamaño de letra, interlineado, títulos y subtítulos? ¿las citas se presentan como se establece en la normativa de la universidad? ¿las fuentes de información están actualizadas? ¿se especifican en las bibliografías las fuentes utilizadas? ¿las bibliografías cumplen con las normas establecidas para cada tipo de fuente? ¿las imágenes, fotografías, gráficos y mapas se encuentran referenciado de forma correcto y cuentan con el tamaño establecido?',
        ponderation:15,

    },
    {
        evaluationCriteria:'7.Sintaxis ',
        questions:'¿Está bien escrito el trabajo de titulación? ¿se entienden las ideas del documento en cada una de las secciones? ¿se cumplen con las normas de acentuación y uso de mayúsculas? ¿el documento está redactado en el tiempo verbal adecuado?',
        ponderation:10,

    },
]
export var  ProductEvaluation=[
    {
        evaluationCriteria:'1. Calidad',
        questions:'¿Evidencia la planificación de las acciones y recursos para el desarrollo adecuado del producto? ¿Cumple con todas las fases y procedimientos técnicos requeridos y detallados en el documento de titulación? ¿Cumple con el lenguaje adecuado para el tipo de producto?',
        ponderation:10,

    },
    {
        evaluationCriteria:'2. Pertinencia',
        questions:'¿Resuelve una necesidad? ¿Se evidencia los aspectos de lógica, utilidad, valor y comprensión en relación a una necesidad?¿En qué grado el objetivo del proyecto es consistente con el producto? ¿Se adapta al público que está dirigido?',
        ponderation:10,

    },
    {
        evaluationCriteria:'3. Creatividad e innovación ',
        questions:'¿Se considera original en relación con otros productos del mismo tipo? ¿Estimula la generación de nuevas ideas? ¿Tiene cualidad para transformar o transcender la realidad? ¿Cumple con criterios de estilo que convierte al producto en algo atractivo para el público?',
        ponderation:10,

    },
    {
        evaluationCriteria:'4. Impacto',
        questions:'¿Se evidencia efectos positivos en el ámbito tecnológico, institucional, económico, político, social, cultural y ambiental? Evaluar en función al ámbito que aplique.',
        ponderation:10,

    }
]
# Joker API
A Ktor based REST API to experiment with [LangChain4j](https://docs.langchain4j.dev/)
and LLMs. 

Frontend project here: [joker-frontend](https://github.com/Martinsbl/joker-frontend)



## Config
Place a file called ``app-config.conf`` in folder ``rootProject/config`` with the following content

````hocon
models: {
  azure: {
    key: "your_key",
    endpoint: "your_enpoint",
  }
  openai: {
    key: "demo" # Or your key
  }
}
chatMemoryMaxSize: 5
jokes: {
  topics: ["AI", "Machine Learning", "Blockchain", "Big Data", "Cloud Computing", "IoT", "DevOps", "Agile", "Microservices", "Containerization", "Serverless", "5G", "Edge Computing", "Quantum Computing", "Cybersecurity", "Data Science", "Deep Learning", "Natural Language Processing", "Computer Vision", "Augmented Reality", "Virtual Reality", "Cryptocurrency", "Full-Stack Development", "Front-End", "Back-End", "API", "RESTful", "GraphQL", "NoSQL", "Kubernetes", "Docker", "CI/CD", "Git", "Scrum", "Kanban", "Artificial General Intelligence", "Robotic Process Automation", "Chatbots", "Voice Recognition", "Biometrics", "Predictive Analytics", "Data Mining", "Neural Networks", "TensorFlow", "PyTorch", "React", "Angular", "Vue.js", "Node.js", "Python", "JavaScript", "TypeScript", "Rust", "Go", "Kotlin", "Swift", "Java", "C++", "Ruby", "PHP", "Scala", "Dart", "Flutter", "React Native", "Progressive Web Apps", "Responsive Design", "UX/UI", "Wireframing", "Prototyping", "A/B Testing", "SEO", "SaaS", "PaaS", "IaaS", "Hybrid Cloud", "Multi-Cloud", "Serverless Computing", "Function-as-a-Service", "Microservices Architecture", "Event-Driven Architecture", "Domain-Driven Design", "Test-Driven Development", "Behavior-Driven Development", "Continuous Integration", "Continuous Deployment", "Infrastructure as Code", "GitOps", "AIOps", "MLOps", "DataOps", "FinOps", "Zero Trust Security", "Blockchain-as-a-Service", "Smart Contracts", "Decentralized Applications", "Non-Fungible Tokens", "Web3", "Metaverse", "Digital Twins", "Explainable AI", "Federated Learning", "Transfer Learning", "Reinforcement Learning", "Generative AI", "Large Language Models"]
}
````
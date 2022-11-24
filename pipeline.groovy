pipeline {
    agent any
    parameters {
        string defaultValue: 'Inconnu', description: 'Please provide your name here.', name: 'name'
    }
    stages {
        stage("Prepare workspace") {
            steps {
                bat "del /S /Q *"
                bat "xcopy /S C:\\stage_observation\\my-python-project ."
            }
        }
        stage("Execute scripts") {
            steps {
                bat "python hello.py ${params.name}"
                bat "python Person.py"
            }
        }
        stage('Unit Tests') {
            steps {
                // We are using nose2 because it can export unit tests in JUnit xml format,
                // which is understood by the xunit plugin of Jenkins.
                // To install nose2: "pip install nose2"
                bat "nose2 --plugin nose2.plugins.junitxml --junit-xml Basic_Test"
                junit 'nose2-junit.xml'
            }
        }
    }
}
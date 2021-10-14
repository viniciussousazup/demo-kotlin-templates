from templateframework.runner import run
from templates.kotlin_test.template import TemplateKotlinTest

if __name__ == '__main__':
    run({
        "kotlin-test": TemplateKotlinTest(),
    })

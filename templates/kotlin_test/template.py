from templateframework.metadata import Metadata
from templateframework.template import Template
from templateframework.runner import apply_template


class TemplateKotlinTest(Template):
    def post_hook(self, metadata: Metadata):
        if metadata.sample:
            apply_template(
                "kotlin-jpa",
                metadata.target_path,
                metadata.inputs,
                metadata.sample_folder,
            )   

#!/bin/bash
mvn --batch-mode verify sonar:sonar \
    -Dsonar.gitlab.api_version=v3 \
	-Dsonar.host.url=http://114.55.179.182:9008 \
	-Dsonar.login=developer \
	-Dsonar.password=zxy123456 \
	-Dsonar.java.binaries=target/sonar \
	-Dsonar.analysis.mode=preview \
    -Dsonar.gitlab.project_id=$CI_PROJECT_ID \
    -Dsonar.gitlab.commit_sha=$CI_BUILD_REF \
    -Dsonar.gitlab.ref_name=$CI_BUILD_REF_NAME \
    -Dmaven.test.skip=true \
    -X

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze-preview over."
fi


package za.co.dariel.aws;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.codecommit.Repository;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.Bucket.Builder;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.constructs.Construct;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.UUID;

import static java.util.Arrays.asList;

public class EnvironmentStack extends Stack {


    public EnvironmentStack(@Nullable Construct scope, @Nullable String id, EnvironmentSettings properties) {
        super(scope, id);

        var name = properties.getName();

        var elasticIp = CfnEIP.Builder.create(this, name+"-eip")
                .tags(Collections.singletonList(createTag("purpose", "Elastic IP for Nat Gateway")))
                .build();


        var natGateway = NatProvider.gateway(
                NatGatewayProps
                        .builder()
                        .eipAllocationIds(asList(elasticIp.getAttrAllocationId()))
                        .build()
        );

        var vpc = Vpc.Builder.create(this, name+"-vpc")
                .cidr("10.0.0.0/16")
                .natGateways(1)
                .natGatewayProvider(natGateway)
                .enableDnsSupport(true)
                .maxAzs(2)
                .build();


        var sshSecurityGroup = SecurityGroup.Builder.create(this, "ssh-security-group")
                .vpc(vpc)
                .securityGroupName("ssh-security-group")
                .description("Security Group for only ssh traffic only")
                .allowAllOutbound(true)
                .build();

        sshSecurityGroup.addIngressRule(Peer.ipv4("0.0.0.0/0"), Port.tcp(23), "SSH connections");

        var repository = Repository.Builder.create(this, properties.getRepository())
                .repositoryName(properties.getRepository())
                .description("Repository for the "+name+" project")
                .build();


        File tempFile = null;
        try {
            tempFile = File.createTempFile(name,"spec");
            Files.write(tempFile.toPath(), BuildSpecGenerator.generateBuildSpec(properties).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


        final var bucketName = name + UUID.randomUUID();
        final var bucket = Bucket.Builder.create(this, bucketName)
                .bucketName(bucketName)
                .accessControl(BucketAccessControl.AUTHENTICATED_READ)
                .build();

        final var projectName = (name + "build").trim();
        Project.Builder.create(this, projectName)
                .badge(true)
                .projectName(projectName)
                .description("Code build pipeline for " + properties.getRepository())
                .concurrentBuildLimit(1)
                .source(Source.codeCommit(CodeCommitSourceProps.builder().repository(repository).build()))
                .buildSpec(BuildSpec.fromSourceFilename(tempFile.getAbsolutePath()))
                .artifacts(Artifacts.s3(S3ArtifactsProps.builder().bucket(bucket).build()))
                .build();




    }

    @NotNull
    private CfnTag createTag(String key, String value) {
        return CfnTag.builder().key(key).value(value).build();
    }


}

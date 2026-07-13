## created

mvn archetype:generate \
    -DarchetypeGroupId=org.apache.beam \
    -DarchetypeArtifactId=beam-sdks-java-maven-archetypes-examples \
    -DarchetypeVersion=2.75.0 \
    -DgroupId=org.example \
    -DartifactId=word-count-beam \
    -Dversion="0.1" \
    -Dpackage=org.apache.beam.examples \
    -DinteractiveMode=false
  
sudo snap install google-cloud-cli --classic

cd word-count-beam

mvn clean install

mv https://gitlab.cis.strath.ac.uk/kwb13215/cs987-examples/-/blob/master/datasets/shakespeare/othello.txt 
to samples

adopt code to your env;

play.

 

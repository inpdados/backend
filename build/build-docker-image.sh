#!/usr/bin/env bash

if [ -d "tmp" ]; then
  rm -rf tmp
fi

name=inpd-backend
echo "Building '$name'"

mkdir tmp
unzip -q ../target/universal/package.zip -d tmp
cp Dockerfile tmp
cat << EOF > tmp/run
#!/usr/bin/env bash
/opt/inpd/service/bin/run -Dconfig.file=/opt/inpd/service/shared/conf/local.conf
EOF

chmod +x tmp/run

cd tmp
docker build -t $name:v1.0 .

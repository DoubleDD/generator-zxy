const Generator = require('yeoman-generator');
const os = require('os');
const fs = require('fs');
const path = require('path');
const mkdirp = require('mkdirp');

module.exports = class extends Generator {
    // The name `constructor` is important here
    constructor(args, opts) {
        // Calling the super constructor is important so our generator is correctly set up
        super(args, opts);
        // Next, add your custom code
        this.option('babel'); // This method adds support for a `--babel` flag

        let userInfo = os.userInfo();

        // 项目名称
        this.project = 'demo';
        // 项目报名
        this.package = 'com.demo';
        // 配置文件里面的项目名称，由project变化而来
        this.appName = 'demo';
        // 用户名
        this.user = userInfo.username;
    }

    async prompting() {
        this.settings = await this.prompt([{
                type: "input",
                name: "project",
                message: "Your project name",
                default: this.project // 项目路径
            },
            {
                type: "input",
                name: "package",
                message: "Your project package",
                default: this.package // Default to current package name
            },
            {
                type: "input",
                name: "user",
                message: "Your username",
                default: this.user
            }
        ]);
    }

    writing() {
        // 源文件根路径
        let sourceRoot = this.templatePath('demo');
        // 目标根路径
        let targetRoot = this.destinationPath(this.settings.project);
        // 模板数据
        let data = {
            project: this.settings.project,
            package: this.settings.package,
            appName: this.settings.project,
            user: this.settings.user
        };
        this.log('sourceRoot=' + sourceRoot)
        this.log('targetRoot=' + targetRoot)
        this.log(data)
        // step1.获取demo文件夹下的非目录文件，将非目录文件拷贝出来
        this._private_step1(sourceRoot, targetRoot, data)

        // step2.copy demo-api 文件夹
        this._private_step2(path.normalize(sourceRoot + '/demo-api'), targetRoot, data)

        // step3.copy demo-async-service 文件夹

        // step4.copy demo-service 文件夹

        // step5.copy demo-web-server 文件夹





        // this.fs.copyTpl(source, target, module);
    }

    /**
     * step1.获取demo文件夹下的非目录文件，将非目录文件拷贝出来
     * @param {*} path 
     */
    _private_step1(source, target, data) {
        this.log('step1.将非目录文件拷贝出来')
        this.log('source=' + source)

        fs.readdir(source, (err, files) => {
            if (err) {
                return console.error(err);
            }

            files.forEach(file => fs.stat(path.normalize(source + '/' + file), (err, stats) => {
                if (err) {
                    return console.error(err);
                }

                let sourceFilePaht = path.normalize(source + '/' + file);
                if (stats.isFile()) {
                    // 将文件拷贝出来
                    let targetFile = path.normalize(target + '/' + file);
                    this.log(targetFile)
                    this.fs.copyTpl(sourceFilePaht, targetFile, data);
                }
            }))
        })
    }

    /**
     * step2.copy demo-api 文件夹
     * @param {*} source 
     * @param {*} target 
     * @param {*} data 
     */
    _private_step2(source, target, data) {
        this.log('step2.copy demo-api 文件夹')
        this.log('source=' + source)

        // 创建 api 目录
        let api_path = path.normalize(target + '/' + data.project + '-api');
        let src_path = path.normalize(api_path + '/src');
        let main_path = path.normalize(src_path + '/main');
        let test_path = path.normalize(src_path + '/test');
        let package_path = data.package.split('.').join('/');
        this.log(package_path);
        // 包路径
        let main_package_path = path.normalize(main_path + '/java/' + package_path);
        // 包路径
        let test_package_path = path.normalize(test_path + '/java/' + package_path);

        // 复制 /src/main/java/demo/ 下的文件
        let source_main_package_path = path.normalize(source + '/src/main/java/demo');
        this.fs.copyTpl(source_main_package_path, main_package_path, data);

        // 复制 /src/pom.xml 下的文件
        let source_pom = path.normalize(source + '/src/pom.xml');
        this.fs.copyTpl(source_pom, src_path, data);

        mkdirp(test_package_path, (err) => {
            if (err) throw err;
        });
    }


    /**
     * step2.copy demo-api 文件夹
     * @param {*} source 
     * @param {*} target 
     * @param {*} data 
     */
    _private_step2(source, target, data) {
        this.log('step2.copy demo-api 文件夹')
        this.log('source=' + source)

        // 创建 api 目录
        let api_path = path.normalize(target + '/' + data.project + '-api');
        let src_path = path.normalize(api_path + '/src');
        let main_path = path.normalize(src_path + '/main');
        let test_path = path.normalize(src_path + '/test');
        let package_path = data.package.split('.').join('/');
        this.log(package_path);
        // 包路径
        let main_package_path = path.normalize(main_path + '/java/' + package_path);
        // 包路径
        let test_package_path = path.normalize(test_path + '/java/' + package_path);

        // 复制 /src/main/java/demo/ 下的文件
        let source_main_package_path = path.normalize(source + '/src/main/java/demo');
        this.fs.copyTpl(source_main_package_path, main_package_path, data);

        // 复制 /src/ 下的其他文件
        let source_src = path.normalize(source + '/src');
        fs.readdir(source_src, (err, files) => {
            if (err) {
                return console.error(err);
            }

            files.forEach(file => fs.stat(path.normalize(source_src + '/' + file), (err, stats) => {
                if (err) {
                    return console.error(err);
                }

                let sourceFilePaht = path.normalize(source_src + '/' + file);
                if (stats.isFile()) {
                    // 将文件拷贝出来
                    let targetFile = path.normalize(source_src + '/' + file);
                    this.log(targetFile)
                    this.fs.copyTpl(sourceFilePaht, targetFile, data);
                }
            }))
        })

        mkdirp(test_package_path, (err) => {
            if (err) throw err;
        });
    }

};
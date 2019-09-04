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
        this.subdirs = [
            'demo-api',
            'demo-async-service',
            'demo-service',
            'demo-web-server'
        ];
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
        // step1.获取demo文件夹下的非目录文件，将非目录文件拷贝出来
        this._private_step1(sourceRoot, targetRoot, data)

        this.subdirs.forEach(name => {
            let suffix = name.replace('demo', '');
            this._private_step3(path.normalize(sourceRoot + '/' + name), suffix, targetRoot, data)
        });
    }

    /**
     * step1.获取demo文件夹下的非目录文件，将非目录文件拷贝出来
     * @param {*} path 
     */
    _private_step1(source, target, data) {
        // this.log('step1.将非目录文件拷贝出来')

        fs.readdir(source, (err, files) => {
            if (err) {
                return console.error(err);
            }

            files.forEach(file => {
                if (file.indexOf('demo-') > -1) {
                    return;
                }
                // 将文件拷贝出来
                let sourceFilePaht = path.normalize(source + '/' + file);
                let targetFile = path.normalize(target + '/' + file);
                this.fs.copyTpl(sourceFilePaht, targetFile, data);
            })
            // this.log('step1.将非目录文件拷贝出来 done!')
        })
    }

    /**
     * step2.copy demo-api 文件夹
     * @param {*} source 
     * @param {*} target 
     * @param {*} data 
     */
    _private_step3(source, suffix, target, data) {
        // this.log(`step2.copy ${source} 文件夹`)

        // 创建 api 目录
        let api_path = path.normalize(target + '/' + data.project + suffix);
        let src_path = path.normalize(api_path + '/src');
        let main_path = path.normalize(src_path + '/main');
        let test_path = path.normalize(src_path + '/test');
        let package_path = data.package.split('.').join('/');

        // 包路径 mian文件夹
        let main_package_path = path.normalize(main_path + '/java/' + package_path);

        // 复制 /src/main/java/demo/ 下的文件
        let source_main_package_path = path.normalize(source + '/src/main/java/demo');
        // this.log('拷贝main目录：' + source_main_package_path)
        this.fs.copyTpl(source_main_package_path, main_package_path, data);

        // 复制 /demo-api/ 下的其他文件
        fs.readdir(source, (err, files) => {
            if (err) {
                return console.error(err);
            }

            files.forEach(file => {
                if (file != 'src') {
                    let sourceFilePaht = path.normalize(source + '/' + file);
                    // this.log('复制src并级文件：' + file + '->' + src_path)
                    this.fs.copyTpl(sourceFilePaht, path.normalize(src_path + '/' + file), data);
                }
            })
        })


        // 包路径 test文件夹
        let test_package_path = path.normalize(test_path + '/java/' + package_path);
        // this.log('创建测试目录：' + test_package_path)
        mkdirp(test_package_path, (err) => {
            if (err) throw err;
        });
    }
};
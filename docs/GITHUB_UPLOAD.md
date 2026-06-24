# GitHub 上传步骤

## 初始化仓库

```bash
git init
git add .
git commit -m "init springboot template"
git branch -M main
```

## 关联远程仓库

```bash
git remote add origin git@github.com:你的用户名/springboot-template.git
git push -u origin main
```

## 建议

- 仓库名使用 `springboot-template`。
- 不要在仓库名、包名、文档中出现具体公司名。
- 不要提交 IDE 临时文件。
- 不要提交本地日志和构建产物。

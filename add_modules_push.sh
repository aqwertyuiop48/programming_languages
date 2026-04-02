#!/bin/bash
# 1. Set global config ONCE outside the loop
git config --global credential.https://github.com.username aqwertyuiop48

# Format: "path|url|branch"
modules=(
"gate|https://github.com/aqwertyuiop48/gate.git|main"
"profiles|https://github.com/aqwertyuiop48/profiles.git|main"
"credentials_/credentials|https://github.com/aqwertyuiop48/credentials.git|main"
"javascript/vue_/vuejs_training|https://github.com/aqwertyuiop48/vuejs_training.git|main"
"javascript/vue_/vuejs_training_typescript|https://github.com/aqwertyuiop48/vuejs_training.git|typescript"
"Python/flask_/vercel_flask_app|https://github.com/aqwertyuiop48/vercel_flask_app.git|main"
"javascript/angular_/my_angular_app|https://github.com/aqwertyuiop48/my_angular_app.git|main"
"javascript/angular_/my_angular_app_main|https://github.com/aqwertyuiop48/my_angular_app.git|main_"
"javascript/nest_/nestjs_app|https://github.com/aqwertyuiop48/nestjs_app.git|main"
"javascript/next_/nextjs_app|https://github.com/aqwertyuiop48/nextjs_app.git|main"
"javascript/next_/nextjs_app_typescript|https://github.com/aqwertyuiop48/nextjs_app.git|typescript"
"javascript/next_/nextjs_app_ts_js|https://github.com/aqwertyuiop48/nextjs_app.git|ts_js"
"javascript/react_/my_react_app|https://github.com/aqwertyuiop48/my_react_app.git|main"
"javascript/react_/my_react_app_main|https://github.com/aqwertyuiop48/my_react_app.git|main_"
"javascript/express_/express_vercel_app|https://github.com/aqwertyuiop48/express_vercel_app.git|main"
"javascript/vue_/vue_project|https://github.com/aqwertyuiop48/vue_project.git|main"
"javascript/express_/JavaScript-Applications|https://github.com/aqwertyuiop48/JavaScript-Applications.git|main"
"typescript/solid_/solid_app|https://github.com/aqwertyuiop48/solid_app.git|main"
"javascript/ionic__/ionic_app|https://github.com/aqwertyuiop48/ionic_app.git|main"
"javascript/app_clones/whatsapp-2|https://github.com/aqwertyuiop48/whatsapp-2.git|main"
"javascript/app_clones/jspaint|https://github.com/aqwertyuiop48/jspaint.git|main"
"javascript/app_clones/Video-Meeting|https://github.com/aqwertyuiop48/Video-Meeting.git|main"
"javascript/app_clones/google-translate-clone|https://github.com/aqwertyuiop48/google-translate-clone.git|main"
"javascript/app_clones/codepen|https://github.com/aqwertyuiop48/codepen.git|main"
"Python/django_/vercel_django_example1|https://github.com/aqwertyuiop48/vercel_django_example1.git|main"
"javascript/koa_/koa_project|https://github.com/aqwertyuiop48/koa_project.git|main"
"typescript/koa_/koa_project|https://github.com/aqwertyuiop48/koa_project.git|typescript"
"javascript/hapi_/hapijs_vercel|https://github.com/aqwertyuiop48/hapijs_vercel.git|main"
"Python/python_server_/python_server|https://github.com/aqwertyuiop48/python_server.git|main"
"javascript/node_/node_server|https://github.com/aqwertyuiop48/node_server.git|main"
"javascript/brunch_/brunch|https://github.com/aqwertyuiop48/brunch.git|main"
"javascript/stencil_/stencil|https://github.com/aqwertyuiop48/stencil.git|main"
"javascript/umijs_/umijs|https://github.com/aqwertyuiop48/umijs.git|main"
"javascript/ember_/ember|https://github.com/aqwertyuiop48/ember.git|main"
"javascript/svelte_/svelte|https://github.com/aqwertyuiop48/svelte.git|main"
"javascript/gatsby_/gatsby|https://github.com/aqwertyuiop48/gatsby.git|main"
"javascript/remix_/remix|https://github.com/aqwertyuiop48/remix.git|main"
"javascript/backbone_/backbone_vercel|https://github.com/aqwertyuiop48/backbone_vercel.git|main"
"javascript/knockout_/knockout_vercel|https://github.com/aqwertyuiop48/knockout_vercel.git|main"
"typescript/ts_node_server/ts_node_server|https://github.com/aqwertyuiop48/ts_node_server.git|main"
"typescript/express_/ts_express_vercel_app|https://github.com/aqwertyuiop48/ts_express_vercel_app.git|main"
"typescript/nest_/nestjs_js|https://github.com/aqwertyuiop48/nestjs_js.git|main"
"golang/golang_/golang_vercel|https://github.com/aqwertyuiop48/golang_vercel.git|main"
"typescript/qwik_/qwik-app|https://github.com/aqwertyuiop48/qwik-app.git|main"
"typescript/nest_/nestjs_tsx|https://github.com/aqwertyuiop48/nestjs_tsx.git|main"
"Python/flask_/flask_pythonanywhere|https://github.com/aqwertyuiop48/flask_pythonanywhere.git|main"
"Python/fastapi_/fastapi_python|https://github.com/aqwertyuiop48/fastapi_python.git|main"
"javascript/graphql_/apollo_graphql|https://github.com/aqwertyuiop48/apollo_graphql.git|main"
"typescript/solid_/solid_ts/solid_app|https://github.com/aqwertyuiop48/solid_app.git|typescript"
"javascript/nuxt_/nuxtjs-boilerplate|https://github.com/aqwertyuiop48/nuxtjs-boilerplate.git|main"
"javascript/redwood_/netlify-deploy|https://github.com/aqwertyuiop48/netlify-deploy.git|main"
"javascript/adonis/serverless-adonis|https://github.com/aqwertyuiop48/serverless-adonis.git|main"
"typescript/redwood_/netlify-deploy|https://github.com/aqwertyuiop48/netlify-deploy.git|typescript"
"typescript/adonis_/serverless-adonis|https://github.com/aqwertyuiop48/serverless-adonis.git|typescript"
"javascript/next_/nextjs_project|https://github.com/aqwertyuiop48/nextjs_project.git|main"
"typescript/adonis_/serverless-adonis-ts|https://github.com/aqwertyuiop48/serverless-adonis-ts.git|main"
"javascript/nx_/nx-monorepo|https://github.com/aqwertyuiop48/nx-monorepo.git|main"
"javascript/next_/nextjs-fastapi-starter-1|https://github.com/aqwertyuiop48/nextjs-fastapi-starter-1.git|main"
"javascript/codeforces_web|https://github.com/aqwertyuiop48/codeforces_web.git|main"
"javascript/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|main"
"javascript/main_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|main_"
"java/selenium_|https://github.com/aqwertyuiop48/selenium_java.git|main"
"javascript/java_embed/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|java_"
"java/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|javac_"
"java/java_web/test-swing11|https://github.com/aqwertyuiop48/test-swing11.git|gh-pages"
"kotlin/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|kotlin1_"
"CPP/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|cpp_"
"julia_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|julia_"
"Python/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|python_"
"ruby/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|ruby_"
"rust/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|rust_"
"elm/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|elm_"
"kotlin/java_embed/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|kotlin_"
"dart/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|dart_"
"csharp/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|c_sharp"
"lua__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|lua_"
"objective_c_cpp/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|objective_c_"
"solidity__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|solidity_"
"swift_code/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|swift_"
"matlab__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|matlab_"
"scilab__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|scilab_"
"java/cucumber_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|java_cucumber"
"scala/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|scala_"
"golang/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|golang_"
"perl/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|perl_"
"pascal/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|pascal_"
"QA/cypress_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|cypress_testing"
"QA/cypress_1/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|cypress_testing_1"
"R__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|R_"
"AI_nization/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|bito_"
"AI_nization/bito1__/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|bito1_"
"azure_/Microsoft_AZ-900|https://github.com/aqwertyuiop48/Microsoft_AZ-900.git|main"
"azure_/main_/Microsoft_AZ-900|https://github.com/aqwertyuiop48/Microsoft_AZ-900.git|main_"
"clojure_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|clojure_"
"clojure_/clojure_script_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|clojure_script"
"certifications|https://github.com/aqwertyuiop48/certifications.git|main"
"javascript/middleman_/middleman|https://github.com/aqwertyuiop48/middleman_.git|main"
"ruby/jekyll1|https://github.com/aqwertyuiop48/jekyll1.git|main"
"java/micronaut_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|micronaut_java_"
"kotlin/micronaut_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|micronaut_kotlin_"
"java/helidon/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|helidon_java_"
"kotlin/helidon_/codeforces_script|https://github.com/aqwertyuiop48/codeforces_script.git|helidon_kotlin_"
"java/http4j_/http4j_java|https://github.com/aqwertyuiop48/http4j_java.git|main"
"kotlin/http4j_/http4j_java|https://github.com/aqwertyuiop48/http4j_java.git|kotlin_"
"javascript/next_/nextjs_news_search_microservices|https://github.com/aqwertyuiop48/nextjs_news_search_microservices.git|main"
"Python/selenium_/selenium_python_example|https://github.com/aqwertyuiop48/selenium_python_example.git|main"
"Python/selenium_/SeleniumBase|https://github.com/aqwertyuiop48/SeleniumBase.git|main"
"java/android_/automated-build-android-app-with-github-action|https://github.com/aqwertyuiop48/automated-build-android-app-with-github-action.git|main"
"java/android_/testing-samples|https://github.com/aqwertyuiop48/testing-samples.git|main"
"java/angular_springboot/Expense_Tracker|https://github.com/aqwertyuiop48/Expense_Tracker.git|main"
"java/angular_springboot/angular-springboot-crud|https://github.com/aqwertyuiop48/angular-springboot-crud.git|main"
"java/angular_springboot/spring-boot-angular-15-mysql-example|https://github.com/aqwertyuiop48/spring-boot-angular-15-mysql-example.git|main"
# --- NEW FRAMEWORKS SECTION ---
"javascript/new_frameworks/vuepress|https://github.com/aqwertyuiop48/vuepress.git|main"
"javascript/new_frameworks/zola|https://github.com/aqwertyuiop48/zola.git|main"
"javascript/new_frameworks/tanstack-start|https://github.com/aqwertyuiop48/tanstack-start.git|main"
"javascript/new_frameworks/vitepress|https://github.com/aqwertyuiop48/vitepress.git|main"
"javascript/new_frameworks/storybook|https://github.com/aqwertyuiop48/storybook.git|main"
"javascript/new_frameworks/preact|https://github.com/aqwertyuiop48/preact.git|main"
"javascript/new_frameworks/parcel|https://github.com/aqwertyuiop48/parcel.git|main"
"javascript/new_frameworks/polymer|https://github.com/aqwertyuiop48/polymer.git|main"
"javascript/new_frameworks/nitro|https://github.com/aqwertyuiop48/nitro.git|main"
"javascript/new_frameworks/ionic-angular|https://github.com/aqwertyuiop48/ionic-angular.git|main"
"javascript/new_frameworks/hydrogen|https://github.com/aqwertyuiop48/hydrogen.git|main"
"javascript/new_frameworks/hugo|https://github.com/aqwertyuiop48/hugo.git|main"
"javascript/new_frameworks/hono|https://github.com/aqwertyuiop48/hono.git|main"
"javascript/new_frameworks/hexo|https://github.com/aqwertyuiop48/hexo.git|main"
"javascript/new_frameworks/h3|https://github.com/aqwertyuiop48/h3.git|main"
"javascript/new_frameworks/elysia|https://github.com/aqwertyuiop48/elysia.git|main"
"javascript/new_frameworks/eleventy|https://github.com/aqwertyuiop48/eleventy.git|main"
"javascript/new_frameworks/docusaurus-2|https://github.com/aqwertyuiop48/docusaurus-2.git|main"
"javascript/new_frameworks/docusaurus|https://github.com/aqwertyuiop48/docusaurus.git|main"
"kotlin/algorithms/Kotlin|https://github.com/aqwertyuiop48/Kotlin.git|master"
"Python/algorithms/Python|https://github.com/aqwertyuiop48/Python.git|master"
"java/algorithms/Java|https://github.com/aqwertyuiop48/Java.git|master"
"algorithms/CLRS|https://github.com/aqwertyuiop48/CLRS.git|main"
"javascript/saas-microservices|https://github.com/aqwertyuiop48/saas-microservices.git|main"
"javascript/turborepo-with-hono|https://github.com/aqwertyuiop48/turborepo-with-hono.git|main"
"rust/rust-axum|https://github.com/aqwertyuiop48/rust-axum.git|main"
"Python/empathic-voice-interface-starter|https://github.com/aqwertyuiop48/empathic-voice-interface-starter.git|main"
"java/temporal/edu-101-java-code|https://github.com/aqwertyuiop48/edu-101-java-code.git|main"
)

for entry in "${modules[@]}"; do
    IFS="|" read -r path url branch <<< "$entry"

    # Create parent directory if not exists
    mkdir -p "$(dirname "$path")"

    # Add submodule
    echo "Adding submodule: $path"

    git config --global credential.https://github.com.username aqwertyuiop48


    git submodule add -b "$branch" "$url" "$path"
    echo
done


# submodule pull and push
git submodule foreach '
BRANCH=$(git config -f $toplevel/.gitmodules submodule.$name.branch || echo main) \
&& git switch $BRANCH \
&& git pull origin $BRANCH \
&& git add . \
&& (git commit -m "Update internal URLs" || echo "No changes in $name") \
&& git push origin $BRANCH \
|| echo "FAILED: $name"
'

# # submodule ONLY push
# git submodule foreach '
# BRANCH=$(git config -f $toplevel/.gitmodules submodule.$name.branch || echo main) \
# && git switch $BRANCH \
# && git add . \
# && (git commit -m "Update internal URLs" || echo "No changes in $name") \
# && git push origin $BRANCH \
# || echo "FAILED: $name"
# '

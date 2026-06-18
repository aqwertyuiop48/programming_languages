git config --global pull.rebase false   # always merge on pull (no rebase)
git pull origin main --allow-unrelated-histories

git submodule foreach --recursive '
git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"
git fetch --all
branch=$(git config -f $toplevel/.gitmodules submodule.$name.branch || echo "main")
git switch $branch
git pull origin $branch --allow-unrelated-histories
'

git add .
if ! git diff --cached --quiet; then
git commit -m "Sync submodules"
git push origin main
else
echo "No changes to commit."
fi
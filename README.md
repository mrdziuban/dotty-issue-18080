# [dotty issue 18080](https://github.com/lampepfl/dotty/issues/18080) reproduction

To demonstrate the issue, clone this repo, `cd` into it, then take the following steps:

1. Wipe out all `target` directories -- `find . -name target -type d | xargs rm -rf`
2. Run `sbt`
3. From the `sbt` shell run `compile`
4. Change [line 21 of `src/main/scala/database/Queries.scala`](src/main/scala/database/Queries.scala#L21). The change should have no affect on the signature or public-facing API:
    ```scala
    // before
    def call: String = s"AVG(${column.name})"
    // after
    def call: String = s"AVG2(${column.name})"
    ```
5. Run `compile` again

On step 5, you will see output like this:

```
[info] compiling 1 Scala source to /Users/matt/dotty-issue-18080/target/scala-3.3.1-RC2/classes ...
[info] done compiling
[info] compiling 3 Scala sources to /Users/matt/dotty-issue-18080/target/scala-3.3.1-RC2/classes ...
[info] done compiling
[success] Total time: 1 s, completed Jul 3, 2023, 3:16:32 PM
```

which indicates that the issue exists. After `Queries.scala` is recompiled (the "1 Scala source" in the first line),
all sources are recompiled due to changes to generated names context bound parameters. See below for more info about the debug output.

## Debug output

The sbt debug output shows that the only changes to the public-facing API of `Queries.scala` are the generated names of context bound parameters:

![sbt api diff](https://github.com/lampepfl/dotty/assets/4718399/7b694a06-18af-4e10-a304-bac4cd3e5fcf)

To view the debug output, exit `sbt` and re-run it with `SBT_DEBUG=1 sbt`, then follow the steps above again.

You can find the changes to the API by searching the output for "[diff]" and looking for red/green text like in the screenshot above.

## Test script

I've also included a script to test whether the issue exists. You can run it with

```bash
./test.sh
```

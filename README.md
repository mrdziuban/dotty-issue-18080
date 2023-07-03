# [dotty issue 18080](https://github.com/lampepfl/dotty/issues/18080) reproduction

To demonstrate the issue:

1. Clone this repo
2. `cd` into it
3. Wipe out all `target` directories -- `find . -name target -type d | xargs rm -rf`
4. Run `sbt`
5. From the `sbt` shell run `compile`
6. Change [line 21 of `src/main/scala/database/Queries.scala`](src/main/scala/database/Queries.scala#L21). The change should have no affect on the signature or public-facing API:
    ```scala
    // before
    def call: String = s"AVG(${column.name})"
    // after
    def call: String = s"AVG2(${column.name})"
    ```
7. Run `compile` again

On step 7, you will see output like this:

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

To view the debug output:

1. Uncomment [lines 3 and 4 of `build.sbt`](build.sbt#L3-L4)
2. Reload `sbt`
3. Follow steps 3-7 above

You can find the changes to the API by searching the output for "[diff]" and looking for red/green text like in the screenshot above.

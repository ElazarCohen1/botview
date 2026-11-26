// Données de démo : à supprimer quand le backend répondra.
import type {ReviewFile} from "../../types/Review";

export const mockReviewFiles: ReviewFile[] = [
    {
        id: 1,
        path: "backend/src/main/java/demo/UserService.java",
        status: "MODIFIED",
        patch: [
            "@@ -10,6 +10,7 @@ public class UserService {",
            "   private final DataSource dataSource;",
            " ",
            "   public User find(String name) throws SQLException {",
            "-    var query = \"SELECT * FROM users WHERE name = ?\";",
            "+    var query = \"SELECT * FROM users WHERE name = '\" + name + \"'\";",
            "+    System.out.println(query);",
            "     return execute(query);",
            "   }",
            "@@ -40,3 +41,4 @@ public class UserService {",
            "   private void log(String message) {",
            "+    // TODO",
            "     logger.info(message);",
            "   }",
        ].join("\n"),
        comments: [
            {id: 1, line: 13, side: "NEW", comment: "Injection SQL : utilise un PreparedStatement."},
            {id: 2, line: 14, side: "NEW", comment: "Évite System.out.println, passe par le logger."},
        ],
    },
    {
        id: 2,
        path: "frontend/src/lib/format.ts",
        status: "ADDED",
        patch: [
            "@@ -0,0 +1,3 @@",
            "+export function format(value: any): string {",
            "+    return String(value);",
            "+}",
        ].join("\n"),
        comments: [{id: 3, line: 1, side: "NEW", comment: "Évite le type any."}],
    },
    {
        id: 3,
        path: "docs/schema.png",
        status: "ADDED",
        patch: null,
        comments: [],
    },
];

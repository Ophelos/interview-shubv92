import { Router } from "express";
import { validate } from "../../middleware/validate";
import { createStatementSchema } from "./statement.schema";
import { createStatementHandler, listStatementsHandler } from "./statement.controller";
import { requireAuth } from "../../middleware/auth";
import transactionRoutes from "../transactions/transaction.routes";

const router = Router({ mergeParams: true });

router.use(requireAuth);
router.post("/", validate(createStatementSchema), createStatementHandler);
router.get("/", listStatementsHandler);
router.use("/:sid/transactions", transactionRoutes);

export default router;

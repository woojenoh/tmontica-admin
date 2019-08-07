import { TCommonError } from "../types/error";

export class CommonError implements TCommonError {
  timestamp?: string;
  status?: number;
  error?: string;
  path?: string;
  message?: string;
  field?: string;
  exceptionMessage?: string;
  errors?: any;

  constructor({
    timestamp,
    status,
    error,
    path,
    message,
    field,
    exceptionMessage,
    errors
  }: TCommonError) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.path = path;
    this.message = message;
    this.field = field;
    this.exceptionMessage = exceptionMessage;
    this.errors = errors;
  }

  alertMessage(customMessage?: string) {
    const message =
      typeof customMessage !== "undefined"
        ? customMessage
        : typeof this.message !== "undefined"
        ? this.message
        : typeof this.exceptionMessage !== "undefined"
        ? this.exceptionMessage
        : null;
    if (!message) {
      return;
    }
    window.alert(this.message);
  }
}

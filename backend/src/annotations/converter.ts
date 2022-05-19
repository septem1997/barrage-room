export function PageListConvert(target: any, propertyKey: string, descriptor: TypedPropertyDescriptor<any>){
    const originalMethod = descriptor.value; // save a reference to the original method
    descriptor.value = async function (...args: any[]) {
        const result = await originalMethod.apply(this, args);
        return {
            data: result[0],
            total: result[1]
        };
    };
    return descriptor;
}

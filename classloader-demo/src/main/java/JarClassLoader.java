import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader {

	public static void main(String[] args) throws Exception {
		File file = new File("../classloader-child/target/classloader-child-0.0.1-SNAPSHOT.jar");
		URL url = file.toURI().toURL();// 将File类型转为URL类型，file为jar包路径
		// 得到系统类加载器
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		// 因为URLClassLoader中的addURL方法的权限为protected所以只能采用反射的方法调用addURL方法
		Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		add.setAccessible(true);
		add.invoke(urlClassLoader, new Object[] { url });
		//Class<?> c = Class.forName("类名");

		Class clazz = urlClassLoader.loadClass("com.demo.classloader.child.Test");
		Object o = clazz.newInstance();

		// 通过反射机制调用我们的Test.java中的printToString方法
		Method method = clazz.getMethod("printToString");
		method.invoke(clazz.newInstance());
		System.out.println(o.getClass().getClassLoader().toString());

		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			// 获取类中的方法名字
			String methodName = methods[i].getName();
			System.out.println("MethodName : " + methodName);
			Class<?>[] params = methods[i].getParameterTypes();
			for (int j = 0; j < params.length; j++) {
				// 获取方法中的参数类型
				System.out.println("ParamsType : " + params[j].toString());
			}
		}

	}

}

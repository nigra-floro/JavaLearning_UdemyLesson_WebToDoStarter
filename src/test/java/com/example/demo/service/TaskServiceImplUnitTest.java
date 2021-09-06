package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskDao;

// JUnit 参考サイト：
// ・Mockitoの記述方法
// 　https://qiita.com/en-ken/items/9a7d3dcb937ea912c7e9
// ・JUnitのアノテーションについて
// 　https://qiita.com/tsukakei/items/b892409cf982f1951933

// 「@ExtendWith(MockitoExtention.class)」でJUnit5にMockitoという拡張機能を統合する
@ExtendWith(MockitoExtension.class)
// @DisplayNameの内容はテスト実行時JUnitタブに表示される
@DisplayName("TaskServiceImplの単体テスト")
class TaskServiceImplUnitTest {

	// Mockitoのアノテーション「@Mock」でモック化（疑似可）したいインターフェースを指定する
	@Mock
	private TaskDao dao;

	// Mockitoのアノテーション「@InjectMocks」で
	// テスト対象のクラスに「@Mock」のモックインスタンスを注入する
	// ここでは TaskServiceImpl の dao がモックインスタンスとなる
	@InjectMocks
	private TaskServiceImpl taskServiceImpl;

	// JUnitのアノテーション「@Test」でJUnitにテストメソッドであることを認識させる
	@Test // テストケース
	@DisplayName("テーブルtaskの全件取得で0件の場合のテスト") // テスト名
	
	void testFindAllReturnEmptyList() {

		// 空のリスト
		List<Task> list = new ArrayList<>();

		// モッククラスのI/Oをセット
		// Mockitoのメソッド「when(A.B()).thenReturn(C)」で
		// インスタンスAのメソッドBが呼ばれた場合にCを返すよう設定する
		// Cの型はBの戻り値の型と一致しなければならない）
		when(dao.findAll()).thenReturn(list);

		// サービスを実行
		// ※内部で dao.findAll が呼ばれ、上の「when～thenReturn…」により
		// 　listが返されるはず
		List<Task> actualList = taskServiceImpl.findAll();

		// モックの指定メソッドの実行回数を検査
		// Mockitoのメソッド「verify(A, times(B)).C()」で
		// インスタンスAのメソッドCが呼ばれた回数がB回であるか検査する
		verify(dao, times(1)).findAll();

		// 戻り値の検査(expected, actual)
		// Mockitoのメソッド「assertEquals(A, B)」でBとAの値が一致することを確認
		assertEquals(0, actualList.size());

	}

	@Test // テストケース
	@DisplayName("テーブルTaskの全件取得で2件の場合のテスト") // テスト名
	void testFindAllReturnList() {

		// モックから返すListに2つのTaskオブジェクトをセット
		List<Task> list = new ArrayList<>();
		Task task1 = new Task();
		Task task2 = new Task();
		list.add(task1);
		list.add(task2);

		// モッククラスのI/Oをセット
		when(dao.findAll()).thenReturn(list);

		// サービスを実行
		List<Task> actualList = taskServiceImpl.findAll();

		// モックの指定メソッドの実行回数を検査
		verify(dao, times(1)).findAll();

		// 戻り値の検査(expected, actual)
		assertEquals(2, actualList.size());

	}

	@Test // テストケース
	@DisplayName("タスクが取得できない場合のテスト") // テスト名
	void testGetTaskThrowException() {

		// モッククラスのI/Oをセット
		// Mockitoのメソッド「when(A.B()).thenThrow(C)」で
		// インスタンスAのメソッドBが呼ばれた場合に例外Cを発生させるよう設定する
		when(dao.findById(0)).thenThrow(new EmptyResultDataAccessException(1));

		// タスクが取得できないとTaskNotFoundExceptionが発生することを検査
		// ※taskServiceImpl.getTask(0)実行時、内部でdao.findById(0) が呼ばれ、
		// 　上の「when～thenThrow…」により	EmptyResultDataAccessExceptionが発生するはず
		// 　さらにEmptyResultDataAccessExceptionはcatchされ、
		// 　TaskNotFoundExceptionが発生するはず
		try {
			Optional<Task> task0 = taskServiceImpl.getTask(0);
		} catch (TaskNotFoundException e) {
			// メッセージの確認
			assertEquals(e.getMessage(), "指定されたタスクが存在しません");
		}
	}

	@Test // テストケース
	@DisplayName("タスクを1件取得した場合のテスト") // テスト名
	void testGetTaskReturnOne() {

		// Taskをデフォルト値でインスタンス化
		Task task = new Task();
		Optional<Task> taskOpt = Optional.ofNullable(task);

		// モッククラスのI/Oをセット
		// ※whenの引数のメソッドには引数（ここでは1）が指定できる。
		// 　ここでは、findByIdが引数1で呼び出された場合taskOptが返される
		when(dao.findById(1)).thenReturn(taskOpt);

		// サービスを実行
		Optional<Task> taskActual = taskServiceImpl.getTask(1);

		// モックの指定メソッドの実行回数を検査
		verify(dao, times(1)).findById(1);

		// Taskが存在していることを確認
		assertTrue(taskActual.isPresent());
	}

	@Test // テストケース ユニットテストではデータベースの例外は考えない
	@DisplayName("削除対象が存在しない場合、例外が発生することを確認するテスト") // テスト名
	void throwNotFoundException() {

		// モッククラスのI/Oをセット
		when(dao.deleteById(0)).thenReturn(0);

		// 削除対象が存在しない場合、例外が発生することを検査
		try {
			taskServiceImpl.deleteById(0);
		} catch (TaskNotFoundException e) {
			assertEquals(e.getMessage(), "削除するタスクが存在しません");
		}

	}

}
